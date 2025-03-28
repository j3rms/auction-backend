package auction.services;

import auction.entities.Category;
import auction.entities.DTO.ItemDTO;
import auction.entities.Item;
import auction.entities.RO.ItemRO;
import auction.entities.User;
import auction.entities.enums.ItemStatus;
import auction.entities.enums.Role;
import auction.entities.utils.MessageUtils;
import auction.exceptions.ServiceException;
import auction.repositories.CategoryRepository;
import auction.repositories.ItemRepository;
import auction.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public List<Item> getAll() {
        try {
            return itemRepository.findAll();
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.retrieveError("Items"), e);
        }
    }

    public List<Item> getAllByFilter(ItemStatus status, Long categoryId) {
        try {
            return itemRepository.findAllByOptionalFilters(status, categoryId);
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.retrieveError("Filtered Items"), e);
        }
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new ServiceException(MessageUtils.notFound("Item"), new RuntimeException("Item not found")));
    }

    @Transactional
    public void save(ItemRO itemRO) {
        try {
            User seller = userRepository.findById(itemRO.getSellerId())
                    .orElseThrow(() -> new ServiceException("Seller not found", new RuntimeException()));

            if (seller.getRole() != Role.SELLER) {
                throw new ServiceException("Only sellers can add items", new RuntimeException());
            }

            Category category = categoryRepository.findById(itemRO.getCategoryId())
                    .orElseThrow(() -> new ServiceException("Category not found", new RuntimeException()));

            Item item = itemRO.toEntity(seller, category);
            item.setImageBase64(itemRO.getImageBase64());

            itemRepository.save(item);
            log.info(MessageUtils.saveSuccess("Item"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.saveError("Item"), e);
        }
    }


    @Transactional
    public void update(Long id, ItemRO itemRO) {
        try {
            Item existingItem = getItemById(id);
            existingItem.updateFromRO(itemRO);
            itemRepository.save(existingItem);
            log.info(MessageUtils.updateSuccess("Item"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.updateError("Item"), e);
        }
    }

    @Transactional
    public ItemDTO updateItemStatus(Long itemId, Long adminId, ItemStatus status) {
        try {
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new ServiceException("Item not found", new RuntimeException()));

            User admin = userRepository.findById(adminId)
                    .orElseThrow(() -> new ServiceException("Admin not found", new RuntimeException()));

            if (admin.getRole() != Role.ADMIN) {
                throw new ServiceException("Only admins can change item status", new RuntimeException());
            }

            item.setStatus(status);
            item.setApprovedAt(status == ItemStatus.APPROVED ? LocalDateTime.now() : null);
            item.setAdmin(status == ItemStatus.APPROVED ? admin : null);

            itemRepository.save(item);
            return new ItemDTO(item);
        } catch (Exception e) {
            throw new ServiceException("Failed to change item status", e);
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            Item item = getItemById(id);
            itemRepository.delete(item);
            log.info(MessageUtils.deleteSuccess("Item"));
        } catch (Exception e) {
            throw new ServiceException(MessageUtils.deleteError("Item"), e);
        }
    }
}

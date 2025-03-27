package auction.services;

import auction.entities.Category;
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

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    public List<Item> getAllByStatus(ItemStatus status) {
        try {
            return itemRepository.findAllByStatus(status);
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

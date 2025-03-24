package auction.services;

import auction.entities.Item;
import auction.entities.RO.ItemRO;
import auction.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    @Autowired
    private ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Optional<Item> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    public Item createItem(ItemRO itemRO) {
        Item item = new Item();
        item.updateFromRO(itemRO);
        return itemRepository.save(item);
    }

    public Item updateItem(Long id, ItemRO itemRO) {
        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            Item item = existingItem.get();
            item.updateFromRO(itemRO);
            return itemRepository.save(item);
        }
        throw new RuntimeException("Item not found");
    }

    public void deleteItem(Long id) {
        itemRepository.deleteById(id);
    }
}
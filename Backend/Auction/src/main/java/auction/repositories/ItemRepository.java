package auction.repositories;

import auction.entities.Item;
import auction.entities.enums.ItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByStatus(String status);

    List<Item> findAllByStatus(ItemStatus status);
}

package auction.repositories;

import auction.entities.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByItemId(Long itemId);

    List<Bid> findByUserId(Long userId);
}

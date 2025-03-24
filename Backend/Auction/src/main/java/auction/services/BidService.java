package auction.services;

import auction.entities.Bid;
import auction.entities.Item;
import auction.entities.User;
import auction.entities.RO.BidRO;
import auction.repositories.BidRepository;
import auction.repositories.ItemRepository;
import auction.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Bid placeBid(BidRO bidRO) {
        Item item = itemRepository.findById(bidRO.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
        User user = userRepository.findById(bidRO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Bid bid = new Bid();
        bid.updateFromRO(bidRO, item, user);
        return bidRepository.save(bid);
    }

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public List<Bid> getBidsByItem(Long itemId) {
        return bidRepository.findByItemId(itemId);
    }

    public List<Bid> getBidsByUser(Long userId) {
        return bidRepository.findByUserId(userId);
    }

    public void deleteBid(Long bidId) {
        if (!bidRepository.existsById(bidId)) {
            throw new EntityNotFoundException("Bid not found");
        }
        bidRepository.deleteById(bidId);
    }
}

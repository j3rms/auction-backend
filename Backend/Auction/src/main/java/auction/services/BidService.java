package auction.services;

import auction.entities.Bid;
import auction.entities.Item;
import auction.entities.User;
import auction.entities.enums.Role;
import auction.entities.RO.BidRO;
import auction.repositories.BidRepository;
import auction.repositories.ItemRepository;
import auction.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public Bid placeBid(BidRO bidRO) {
        Item item = itemRepository.findById(bidRO.getItemId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found"));
    
        User customer = userRepository.findById(bidRO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    
        User seller = item.getSeller(); // ✅ Get the seller from the item
    
        // ✅ Ensure only customers can place bids
        if (customer.getRole() != Role.CUSTOMER) {
            throw new IllegalArgumentException("Only customers can place bids.");
        }
    
        // ✅ Get the last highest bid
        Optional<Bid> lastBidOpt = bidRepository.findByItemId(item.getId()).stream()
                .max((b1, b2) -> b1.getBidAmount().compareTo(b2.getBidAmount()));
    
        BigDecimal lastBidAmount = lastBidOpt.map(Bid::getBidAmount).orElse(item.getStartingPrice());
    
        // ✅ Use a default bid increment
        BigDecimal bidIncrement = BigDecimal.ONE;
        BigDecimal minNextBid = lastBidAmount.add(bidIncrement);
    
        // ✅ Ensure the bid follows the increment rule
        if (bidRO.getBidAmount().compareTo(minNextBid) < 0) {
            throw new IllegalArgumentException("Bid must be at least " + minNextBid);
        }
    
        // ✅ Create and save the bid
        Bid bid = new Bid();
        bid.updateFromRO(bidRO, item, customer, seller);
        bid.setBidTime(LocalDateTime.now());
    
        return bidRepository.save(bid);
    }
    

    public List<Bid> getAllBids() {
        return bidRepository.findAll();
    }

    public List<Bid> getBidsByItem(Long itemId) {
        return bidRepository.findByItemId(itemId);
    }

    public List<Bid> getBidsByUser(Long customerId) {  
        return bidRepository.findByCustomerId(customerId);  // ✅ Ensure repository method matches "customer_id"
    }

    public void deleteBid(Long bidId) {
        if (!bidRepository.existsById(bidId)) {
            throw new EntityNotFoundException("Bid not found.");
        }
        bidRepository.deleteById(bidId);
    }
}

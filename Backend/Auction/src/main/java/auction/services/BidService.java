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

        User seller = item.getSeller();
        if (seller == null) {
            throw new IllegalArgumentException("Item must have a seller before bidding.");
        }

        if (customer.getRole() == Role.ADMIN) {
            throw new IllegalArgumentException("Admins cannot place bids.");
        }

        if (customer.getId().equals(seller.getId())) {
            throw new IllegalArgumentException("You cannot bid on your own item.");
        }

        if (customer.getRole() != Role.CUSTOMER && customer.getRole() != Role.SELLER) {
            throw new IllegalArgumentException("Only customers or sellers can place bids.");
        }

        Optional<Bid> lastBidOpt = bidRepository.findByItemId(item.getId()).stream()
                .max((b1, b2) -> b1.getBidAmount().compareTo(b2.getBidAmount()));

        BigDecimal lastBidAmount = lastBidOpt.map(Bid::getBidAmount).orElse(item.getStartingPrice());
        BigDecimal bidIncrement = BigDecimal.ONE;
        BigDecimal minNextBid = lastBidAmount.add(bidIncrement);

        if (bidRO.getBidAmount().compareTo(minNextBid) < 0) {
            throw new IllegalArgumentException("Bid must be at least " + minNextBid);
        }

        Bid bid = new Bid();
        bid.updateFromRO(bidRO, item, customer);
        bid.setSeller(seller);
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
        return bidRepository.findByCustomerId(customerId);
    }

    public void deleteBid(Long bidId) {
        if (!bidRepository.existsById(bidId)) {
            throw new EntityNotFoundException("Bid not found.");
        }
        bidRepository.deleteById(bidId);
    }

    public List<Bid> getAllByFilter(Long itemId, Long customerId) {
        return bidRepository.findByFilter(itemId, customerId);
    }



}

package auction.controllers;

import auction.entities.Bid;
import auction.entities.RO.BidRO;
import auction.services.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @PostMapping
    public ResponseEntity<Bid> placeBid(@RequestBody BidRO bidRO) {
        Bid bid = bidService.placeBid(bidRO);
        return ResponseEntity.ok(bid);
    }

    @GetMapping
    public ResponseEntity<List<Bid>> getAllBids() {
        return ResponseEntity.ok(bidService.getAllBids());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Bid>> getBidsByItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(bidService.getBidsByItem(itemId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bid>> getBidsByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bidService.getBidsByUser(userId));
    }

    @DeleteMapping("/{bidId}")
    public ResponseEntity<Void> deleteBid(@PathVariable Long bidId) {
        bidService.deleteBid(bidId);
        return ResponseEntity.noContent().build();
    }
}

package auction.entities.DTO;

import auction.entities.Bid;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO {
    private Long id;
    private BigDecimal bidAmount;
    private LocalDateTime bidTime;
    private UserDTO customer; // ✅ Only the bidder (customer)
    private ItemDTO item;     // ✅ Item contains the seller inside

    public BidDTO(Bid bid) {
        this.id = bid.getId();
        this.bidAmount = bid.getBidAmount();
        this.bidTime = bid.getBidTime();
        this.customer = new UserDTO(bid.getCustomer()); // ✅ Minimal customer info
        this.item = new ItemDTO(bid.getItem()); // ✅ Uses updated ItemDTO (which includes id)
    }
    
}

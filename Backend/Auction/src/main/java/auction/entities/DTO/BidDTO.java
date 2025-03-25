package auction.entities.DTO;

import auction.entities.Bid;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BidDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private BigDecimal bidAmount;
    private ItemDTO item;
    private UserDTO user;

    public BidDTO(Bid bid) {
        this.bidAmount = bid.getBidAmount();
        this.item = new ItemDTO(bid.getItem());
        this.user = new UserDTO(bid.getUser());
    }
}

package auction.entities.RO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BidRO {
    private Long itemId;
    private Long userId;
    private BigDecimal bidAmount;
}

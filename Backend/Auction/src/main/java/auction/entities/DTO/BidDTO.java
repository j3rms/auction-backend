package auction.entities.DTO;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class BidDTO {
    private Long itemId;
    private BigDecimal amount;
}

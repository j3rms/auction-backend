package auction.entities.RO;

import auction.entities.enums.ItemStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemRO {
    private String name;
    private String description;
    private BigDecimal startingPrice;
    private BigDecimal currentBidPrice;
    private ItemStatus status;
    private LocalDateTime auctionEndTime;
    private Long ownerId;
}
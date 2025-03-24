package auction.entities.DTO;

import auction.entities.enums.ItemStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ItemDTO {
    private String title;
    private String description;
    private BigDecimal startingPrice;
    private ItemStatus status;
    private LocalDateTime auctionEndTime;
}


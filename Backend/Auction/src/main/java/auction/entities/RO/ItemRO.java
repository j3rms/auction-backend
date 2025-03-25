package auction.entities.RO;

import auction.entities.enums.ItemStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRO {
    private String name;
    private String description;
    private BigDecimal startingPrice;
    private ItemStatus status;
    private UserRO seller;

    public auction.entities.Item toEntity() {
        return auction.entities.Item.builder()
                .name(this.name)
                .description(this.description)
                .startingPrice(this.startingPrice)
                .status(this.status)
                .seller(this.seller != null ? this.seller.toEntity() : null)
                .build();
    }
}

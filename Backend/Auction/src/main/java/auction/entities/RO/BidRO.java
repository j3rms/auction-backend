package auction.entities.RO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BidRO {
    private BigDecimal bidAmount;
    private ItemRO item;
    private UserRO user;
    private Long itemId;
    private Long userId;

    public auction.entities.Bid toEntity(auction.entities.Item item, auction.entities.User user) {
        return auction.entities.Bid.builder()
                .bidAmount(this.bidAmount)
                .item(item)
                .user(user)
                .build();
    }
}

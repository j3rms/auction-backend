package auction.entities;

import auction.entities.RO.BidRO;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bids")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bid_id", columnDefinition = "BIGINT")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false, columnDefinition = "BIGINT")
    private Item item;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "BIGINT")
    private User user;

    @Column(name = "bid_amount", nullable = false)
    private BigDecimal bidAmount;

    public void updateFromRO(BidRO bidRO, Item item, User user) {
        this.item = item;
        this.user = user;
        this.bidAmount = bidRO.getBidAmount();
    }
}

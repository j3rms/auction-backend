package auction.entities;

import auction.entities.RO.BidRO;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Bid {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private BigDecimal bidAmount;

    public void updateFromRO(BidRO bidRO, Item item, User user) {
        this.item = item;
        this.user = user;
        this.bidAmount = bidRO.getBidAmount();
    }
}
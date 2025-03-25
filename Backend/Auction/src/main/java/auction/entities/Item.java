package auction.entities;

import auction.entities.RO.ItemRO;
import auction.entities.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id", columnDefinition = "BIGINT")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal startingPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false, columnDefinition = "BIGINT")
    private User seller;

    public void updateFromRO(ItemRO itemRO) {
        this.name = itemRO.getName();
        this.description = itemRO.getDescription();
        this.startingPrice = itemRO.getStartingPrice();
        this.status = itemRO.getStatus();
    }
}

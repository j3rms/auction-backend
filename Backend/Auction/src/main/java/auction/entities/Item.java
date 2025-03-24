package auction.entities;

import auction.entities.RO.ItemRO;
import auction.entities.enums.ItemStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private BigDecimal startingPrice;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    public void updateFromRO(ItemRO itemRO) {
        this.name = itemRO.getName();
        this.description = itemRO.getDescription();
        this.startingPrice = itemRO.getStartingPrice();
        this.status = itemRO.getStatus();
    }
}

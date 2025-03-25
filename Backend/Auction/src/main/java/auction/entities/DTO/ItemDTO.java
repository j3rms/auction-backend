package auction.entities.DTO;

import auction.entities.Item;
import auction.entities.enums.ItemStatus;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private BigDecimal startingPrice;
    private ItemStatus status;
    private UserDTO seller; // Reference to UserDTO

    public ItemDTO(Item item) {
        this.name = item.getName();
        this.description = item.getDescription();
        this.startingPrice = item.getStartingPrice();
        this.status = item.getStatus();
        this.seller = new UserDTO(item.getSeller()); // Convert User to UserDTO
    }
}

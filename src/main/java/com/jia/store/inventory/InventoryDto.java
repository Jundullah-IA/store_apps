package com.jia.store.inventory;

import com.jia.store.common.CommonDto;
import com.jia.store.item.Item;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDto extends CommonDto {
    @NotNull(message = "item_id cannot be empty")
    private UUID itemId;

    @NotNull(message = "quantity cannot be empty")
    @Min(value = 1, message = "Minimum quantity is 1")
    private Integer quantity;

    @NotNull(message = "type cannot be empty")
    private InventoryType type;

    public Inventory toEntity(Item item) {
        Inventory inventory = new Inventory();

        inventory.setItem(item);
        inventory.setQuantity(this.quantity);
        inventory.setType(this.type);

        return inventory;
    }

    public void copyToEntity(Inventory inventory) {
        inventory.setQuantity(copy(this.quantity, inventory.getQuantity()));
        inventory.setType(copy(this.type, inventory.getType()));
    }
}

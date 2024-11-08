package com.jia.store.order;

import com.jia.store.common.CommonDto;
import com.jia.store.item.Item;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto extends CommonDto {
    @NotNull
    private UUID itemId;

    @NotNull
    private Integer quantity;

    public OrderDto(UUID itemId, Integer quantity) {
        this.itemId = itemId;
        this.quantity = quantity;
    }

    public Order toEntity(Item item) {
        Order order = new Order();
        order.setQuantity(this.quantity);

        order.setItem(item);
        order.setPrice(item.getPrice());

        return order;
    }

    public void copyToEntity(Order order) {
        order.setQuantity(copy(quantity, order.getQuantity()));
    }
}

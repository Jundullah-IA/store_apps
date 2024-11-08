package com.jia.store.order;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jia.store.common.CommonEntity;
import com.jia.store.item.Item;
import com.jia.store.item.ItemSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "orders")
@NoArgsConstructor
public class Order extends CommonEntity {
    @ManyToOne
    @JsonSerialize(using = ItemSerializer.class)
    private Item item;

    private Integer quantity;
    private BigDecimal price;

    public Order(UUID id, Item item, Integer quantity, BigDecimal price) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public Order(UUID id, Item item, Integer quantity,
                 BigDecimal price, LocalDateTime deletedAt) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.price = price;
        this.deletedAt = deletedAt;
    }
}

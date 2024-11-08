package com.jia.store.inventory;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.jia.store.common.CommonEntity;
import com.jia.store.item.Item;
import com.jia.store.item.ItemSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "inventories")
@NoArgsConstructor
public class Inventory extends CommonEntity {
    @ManyToOne
    @JsonSerialize(using = ItemSerializer.class)
    private Item item;

    private Integer quantity;

    @Enumerated(EnumType.STRING)
    private InventoryType type;

    public Inventory(UUID id,
                     Item item,
                     Integer quantity,
                     InventoryType type) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.type = type;
    }

    public Inventory(UUID id,
                     Item item,
                     Integer quantity,
                     InventoryType type,
                     LocalDateTime deletedAt) {
        this.id = id;
        this.item = item;
        this.quantity = quantity;
        this.type = type;
        this.deletedAt = deletedAt;
    }
}
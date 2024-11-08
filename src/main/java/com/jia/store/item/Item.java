package com.jia.store.item;

import com.jia.store.common.CommonEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "items")
@NoArgsConstructor
public class Item extends CommonEntity {
    private String name;
    private BigDecimal price;

    public Item(UUID itemId) { this.id = itemId; }
}
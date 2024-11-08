package com.jia.store.item.stock;


import com.jia.store.item.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Immutable
@Subselect("""
WITH inventory_movements AS (
    SELECT item_id, CASE WHEN type = 'T' THEN quantity ELSE -quantity END as quantity
    FROM INVENTORIES WHERE deleted_at IS NULL
), order_movements AS (
    SELECT item_id, -quantity as quantity
    FROM ORDERS WHERE deleted_at IS NULL
), stock_calculation AS (
    SELECT item_id, SUM(quantity) as total_stock
    FROM (
        SELECT item_id, quantity FROM inventory_movements
        UNION ALL
        SELECT item_id, quantity FROM order_movements
    ) stock_movements
    GROUP BY item_id
) SELECT i.id, i.created_at, i.updated_at, i.name, i.price,
    COALESCE(sc.total_stock, 0) AS stock
FROM ITEMS i
LEFT JOIN stock_calculation sc ON i.id = sc.item_id
WHERE i.deleted_at IS NULL
""")
@Getter
@Setter
@NoArgsConstructor
public class ItemStock {
    @Id
    private UUID id;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String name;
    private BigDecimal price;
    private Integer stock;

    public ItemStock(UUID id, Integer stock) {
        this.id = id;
        this.stock = stock;
    }

    public ItemStock(UUID id, Integer stock, BigDecimal price) {
        this.id = id;
        this.stock = stock;
        this.price = price;
    }

    public Item toItem() {
        Item item = new Item(id);
        item.setCreatedAt(createdAt);
        item.setUpdatedAt(updatedAt);
        item.setName(this.name);
        item.setPrice(this.price);
        return item;
    }
}

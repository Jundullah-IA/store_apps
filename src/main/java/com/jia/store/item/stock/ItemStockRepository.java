package com.jia.store.item.stock;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ItemStockRepository extends JpaRepository<ItemStock, UUID> {}

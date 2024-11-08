package com.jia.store.inventory;

import com.jia.store.item.stock.ItemStock;
import com.jia.store.item.stock.ItemStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {
    @Mock
    private InventoryRepository repository;
    @Mock
    private ItemStockRepository stockRepository;
    @InjectMocks
    private InventoryService service;

    private final ItemStock item = new ItemStock(UUID.randomUUID(), 10);

    @Test
    void insert_topup_success() {
        Inventory inventoryT = new Inventory(
                UUID.randomUUID(), item.toItem(), 5, InventoryType.T);
        when(repository.save(any(Inventory.class))).thenReturn(inventoryT);
        when(stockRepository.findById(item.getId())).thenReturn(Optional.of(item));

        InventoryDto dto = new InventoryDto(item.getId(), 5, InventoryType.T);
        Inventory response = (Inventory) service.insert(dto).getContent();

        assertEquals(5, response.getQuantity());
        assertEquals(InventoryType.T, response.getType());
    }

    @Test
    void insert_withdraw_success() {
        Inventory inventoryW = new Inventory(
                UUID.randomUUID(), item.toItem(), 5, InventoryType.W);
        when(repository.save(any(Inventory.class))).thenReturn(inventoryW);
        when(stockRepository.findById(item.getId())).thenReturn(Optional.of(item));

        InventoryDto dto = new InventoryDto(item.getId(), 5, InventoryType.W);
        Inventory response = (Inventory) service.insert(dto).getContent();

        assertEquals(5, response.getQuantity());
        assertEquals(InventoryType.W, response.getType());
    }

    @Test
    void update_success() {
        UUID inventoryId = UUID.randomUUID();
        when(repository.findById(inventoryId))
            .thenReturn(Optional.of(new Inventory(inventoryId, item.toItem(), 5, InventoryType.T)));
        when(repository.save(any(Inventory.class)))
            .thenReturn(new Inventory(inventoryId, item.toItem(), 3, InventoryType.T));
        when(stockRepository.findById(item.getId())).thenReturn(Optional.of(item));

        InventoryDto dto = new InventoryDto(item.getId(), 3, InventoryType.W);
        Inventory response = (Inventory) service.update(inventoryId, dto).getContent();
        assertEquals(3, response.getQuantity());
    }

    @Test
    void delete_success() {
        UUID inventoryId = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now();

        when(repository.findById(inventoryId)).thenReturn(Optional.of(
                new Inventory(inventoryId, item.toItem(), 5, InventoryType.T)));
        when(repository.save(any(Inventory.class))).thenReturn(
                new Inventory(inventoryId, item.toItem(), 5, InventoryType.T, currentTime));

        Inventory response = (Inventory) service.delete(inventoryId).getContent();
        assertNotNull(response.getDeletedAt());
    }
}
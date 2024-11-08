package com.jia.store.order;

import com.jia.store.inventory.InventoryRepository;
import com.jia.store.item.stock.ItemStock;
import com.jia.store.item.stock.ItemStockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository repository;
    @Mock
    private InventoryRepository inventoryRepository;
    @Mock
    private ItemStockRepository stockRepository;
    @InjectMocks
    private OrderService service;

    private final ItemStock stock = new ItemStock(UUID.randomUUID(), 10, BigDecimal.valueOf(12000));

    @Test
    void insert_success() {
        Order order = new Order(UUID.randomUUID(), stock.toItem(), 5, BigDecimal.valueOf(12000));
        when(repository.save(any(Order.class))).thenReturn(order);
        when(stockRepository.findById(stock.getId())).thenReturn(Optional.of(stock));

        Order response = (Order) service.insert(new OrderDto(stock.getId(), 5)).getContent();
        assertEquals(order, response);
    }

    @Test
    void update_success() {
        UUID orderId = UUID.randomUUID();
        when(repository.findById(orderId)).thenReturn(Optional.of(
                new Order(UUID.randomUUID(), stock.toItem(), 5, BigDecimal.valueOf(12000))));
        when(repository.save(any(Order.class))).thenReturn(
                new Order(orderId, stock.toItem(), 7, BigDecimal.valueOf(12000)));
        when(stockRepository.findById(stock.getId())).thenReturn(Optional.of(stock));

        Order order = (Order) service.update(orderId, new OrderDto(stock.getId(), 7)).getContent();
        assertEquals(7, order.getQuantity());
    }

    @Test
    void delete_success() {
        UUID orderId = UUID.randomUUID();
        LocalDateTime currentTime = LocalDateTime.now();
        when(repository.findById(orderId)).thenReturn(Optional.of(
                new Order(UUID.randomUUID(), stock.toItem(), 5, BigDecimal.valueOf(12000))));
        when(repository.save(any(Order.class))).thenReturn(
                new Order(orderId, stock.toItem(), 5, BigDecimal.valueOf(12000), currentTime));

        Order order = (Order) service.delete(orderId).getContent();
        assertNotNull(order.getDeletedAt());
    }
}
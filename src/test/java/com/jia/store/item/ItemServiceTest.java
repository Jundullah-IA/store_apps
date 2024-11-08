package com.jia.store.item;

import com.jia.store.common.CommonController;
import com.jia.store.item.stock.ItemStock;
import com.jia.store.utils.responsemapper.PageResponse;
import com.jia.store.utils.responsemapper.SingleEntityResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository repository;

    @InjectMocks
    private ItemService service;

    private final Item item = new Item();

    @BeforeEach
    public void setUp() {
        item.setId(UUID.randomUUID());
        item.setName("Drawing Book");
        item.setPrice(BigDecimal.valueOf(12000));
    }

    @Test
    void getPage_Success() {
        Page<Item> page = new PageImpl<>(List.of(
                item,
                new Item(UUID.randomUUID()),
                new Item(UUID.randomUUID())));

        Pageable pageable = CommonController.getPageable("createdAt", true, 0, 10);
        when(repository.findAll(pageable)).thenReturn(page);

        PageResponse<ItemStock> response = service.getPage(pageable);
        assertEquals(3, response.getContent().size());
    }

    @Test
    void getSingle_Success() {
        when(repository.findById(item.getId())).thenReturn(Optional.of(item));

        Item result = (Item) service.getSingle(item.getId()).getContent();
        assertEquals(item.getId(), result.getId());
    }

    @Test
    void insert_success() {
        when(repository.save(any(Item.class))).thenReturn(item);

        ItemDto dto = new ItemDto(item.getName(), item.getPrice());
        SingleEntityResponse response = service.insert(dto);

        assertEquals(item, response.getContent(), "Success insert");
    }

    @Test
    void update_success() {
        String updatedName = "Drawing Book";
        BigDecimal updatedPrice = BigDecimal.valueOf(14000);

        Item updatedItem = new Item(item.getId());
        updatedItem.setName(updatedName);
        updatedItem.setPrice(updatedPrice);

        when(repository.findById(item.getId())).thenReturn(Optional.of(item));
        when(repository.save(any(Item.class))).thenReturn(updatedItem);

        ItemDto dto = new ItemDto(updatedName, updatedPrice);
        Item result = (Item) service.update(item.getId(), dto).getContent();

        assertEquals(updatedName, result.getName(), "Success update name");
        assertEquals(updatedPrice, result.getPrice(), "Success update price");
    }

    @Test
    void delete_success() {
        LocalDateTime currentTime = LocalDateTime.now();

        Item deletedItem = new Item(item.getId());
        deletedItem.setName(item.getName());
        deletedItem.setPrice(item.getPrice());
        deletedItem.setDeletedAt(currentTime);

        when(repository.findById(item.getId())).thenReturn(Optional.of(item));
        when(repository.save(any(Item.class))).thenReturn(deletedItem);

        Item result = (Item) service.delete(item.getId()).getContent();

        assertEquals(currentTime, result.getDeletedAt(), "Success delete item");
    }
}

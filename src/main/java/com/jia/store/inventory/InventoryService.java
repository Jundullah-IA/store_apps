package com.jia.store.inventory;

import com.jia.store.item.stock.ItemStock;
import com.jia.store.item.stock.ItemStockRepository;
import com.jia.store.utils.responsemapper.PageResponse;
import com.jia.store.utils.responsemapper.SingleEntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {
    private final InventoryRepository repository;
    private final ItemStockRepository stockRepository;

    private static final String INSUFFICIENT_STOCK_MESSAGE = "Insufficient stock";

    @Transactional(readOnly = true,
                   propagation = Propagation.SUPPORTS,
                   isolation = Isolation.READ_COMMITTED)
    public PageResponse<Inventory> getPage(Pageable pageable) {
        return new PageResponse<>(repository.findAll(pageable));
    }

    @Transactional(readOnly = true,
                   propagation = Propagation.SUPPORTS,
                   isolation = Isolation.READ_COMMITTED,
                   noRollbackFor = ResponseStatusException.class )
    public SingleEntityResponse getSingle(UUID id) {
        return new SingleEntityResponse(findById(id));
     }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED )
    public SingleEntityResponse insert(InventoryDto dto) {
        ItemStock stock = findItemStockById(dto.getItemId());
        if (dto.getType() == InventoryType.W && dto.getQuantity() > stock.getStock())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INSUFFICIENT_STOCK_MESSAGE);

        return new SingleEntityResponse(repository.save(dto.toEntity(stock.toItem())));
    }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.REPEATABLE_READ )
    public SingleEntityResponse update(UUID id,InventoryDto dto) {
        Inventory inventory = findById(id);

        ItemStock item = findItemStockById(inventory.getItem().getId());
        int initialStock = inventory.getType() == InventoryType.T ?
                item.getStock() - dto.getQuantity() : item.getStock() + dto.getQuantity();
        if (dto.getType() == InventoryType.W && dto.getQuantity() > initialStock)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, INSUFFICIENT_STOCK_MESSAGE);

        dto.copyToEntity(inventory);
        return new SingleEntityResponse(repository.save(inventory));
    }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED )
    public SingleEntityResponse delete(UUID id) {
        Inventory inventory = findById(id);
        inventory.setDeletedAt(LocalDateTime.now());
        return new SingleEntityResponse(repository.save(inventory));
    }

    private Inventory findById(UUID id) {
        return repository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Inventory not found"));
    }

    private ItemStock findItemStockById(UUID id) {
        return stockRepository.findById(id).orElseThrow(
            () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Item not found"));
    }
}

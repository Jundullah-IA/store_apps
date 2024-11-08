package com.jia.store.item;

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
public class ItemService {
    private final ItemRepository repository;
    private final ItemStockRepository stockRepository;

    @Transactional(readOnly = true,
                   propagation = Propagation.SUPPORTS,
                   isolation = Isolation.READ_COMMITTED )
    public PageResponse<ItemStock> getPage(Pageable pageable) {
        return new PageResponse<>(stockRepository.findAll(pageable));
    }

    @Transactional(readOnly = true,
                   propagation = Propagation.SUPPORTS,
                   isolation = Isolation.READ_COMMITTED,
                   noRollbackFor = ResponseStatusException.class )
    public SingleEntityResponse getSingle(UUID id) {
        return new SingleEntityResponse(stockRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object not found")));
    }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED )
    public SingleEntityResponse insert(ItemDto dto) {
        return new SingleEntityResponse(repository.save(dto.toEntity()));
    }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.REPEATABLE_READ)
    public SingleEntityResponse update(UUID id,ItemDto dto) {
        Item item = findById(id);
        dto.copyToEntity(item);
        return new SingleEntityResponse(repository.save(item));
    }

    @Transactional(rollbackFor = Exception.class,
                   propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED)
    public SingleEntityResponse delete(UUID id) {
        Item item = findById(id);
        item.setDeletedAt(LocalDateTime.now());
        return new SingleEntityResponse(repository.save(item));
    }

    private Item findById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Object not found"));
    }
}

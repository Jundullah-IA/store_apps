package com.jia.store.item;

import com.jia.store.common.CommonController;
import com.jia.store.item.stock.ItemStock;
import com.jia.store.utils.responsemapper.PageResponse;
import com.jia.store.utils.responsemapper.SingleEntityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/items")
@RequiredArgsConstructor
public class ItemController extends CommonController {
    private final ItemService service;

    @GetMapping()
    public ResponseEntity<PageResponse<ItemStock>> page(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int perPage,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "true") boolean descending) {
        return ResponseEntity.ok(service.getPage(getPageable(sortBy, descending, page, perPage)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SingleEntityResponse> findbyId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getSingle(id));
    }

    @PostMapping
    public ResponseEntity<SingleEntityResponse> create(@RequestBody ItemDto dto) {
        return ResponseEntity.ok(service.insert(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SingleEntityResponse> update(@PathVariable UUID id, @RequestBody ItemDto dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SingleEntityResponse> softDelete(@PathVariable UUID id) {
        return ResponseEntity.ok(service.delete(id));
    }
}

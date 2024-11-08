package com.jia.store.item;

import com.jia.store.common.CommonDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto extends CommonDto {
    @NotBlank
    private String name;

    @NotNull
    private BigDecimal price;

    public Item toEntity() {
        Item item = new Item();
        item.setName(this.name);
        item.setPrice(this.price);
        return item;
    }

    public void copyToEntity(Item item) {
        item.setName(copy(this.name, item.getName()));
        item.setPrice(copy(this.price, item.getPrice()));
    }
}
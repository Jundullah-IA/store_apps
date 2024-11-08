package com.jia.store.common;

import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CommonController {
    public static Sort getSort(String sortBy, boolean descending) {
        Sort.Direction direction = descending ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort.Order sortOrder = Sort.Order.by(sortBy).with(direction).ignoreCase().nullsLast();
        return Sort.by(sortOrder);
    }

    public static Pageable getPageable(String sortBy, boolean descending, int page, int perPage) {
        if (!StringUtils.isEmpty(sortBy) && !StringUtils.isBlank(sortBy))
            return PageRequest.of(page, perPage, getSort(sortBy, descending));
        return PageRequest.of(page, perPage);
    }
}
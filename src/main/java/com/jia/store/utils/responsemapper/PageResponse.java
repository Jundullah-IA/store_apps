package com.jia.store.utils.responsemapper;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Collection;

@Getter
public class PageResponse<E> extends ApiResponse{
    private int currentPage;
    private long totalRows;
    private int totalPages;
    private boolean hasNext;
    private Collection<E> content;

    public PageResponse(Page<E> page) {
        super(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        this.currentPage = page.getNumber();
        this.totalRows = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.hasNext = page.hasNext();
        this.content = page.getContent();
    }
}
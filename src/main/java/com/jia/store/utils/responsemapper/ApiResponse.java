package com.jia.store.utils.responsemapper;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse {
    private LocalDateTime timestamp;
    private String status;
    private int code;

    public ApiResponse(String status, int code) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.code = code;
    }
}

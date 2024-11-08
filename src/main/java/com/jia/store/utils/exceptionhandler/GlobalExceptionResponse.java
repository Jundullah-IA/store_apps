package com.jia.store.utils.exceptionhandler;

import com.jia.store.utils.responsemapper.ApiResponse;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class GlobalExceptionResponse extends ApiResponse {
    private String message;

    public GlobalExceptionResponse(HttpStatus httpStatus) {
        super(httpStatus.getReasonPhrase(), httpStatus.value());
        this.message = httpStatus.getReasonPhrase();
    }

    public GlobalExceptionResponse(HttpStatus httpStatus, String message) {
        super(httpStatus.getReasonPhrase(), httpStatus.value());
        this.message = message;
    }
}

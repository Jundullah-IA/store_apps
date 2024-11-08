package com.jia.store.utils.responsemapper;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SingleEntityResponse extends ApiResponse{
    private Object content;

    public SingleEntityResponse(Object content) {
        super(HttpStatus.OK.getReasonPhrase(), HttpStatus.OK.value());
        this.content = content;
    }
}

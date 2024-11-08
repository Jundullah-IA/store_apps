package com.jia.store.common;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public abstract class CommonDto {
    public String copy(String from, String to) {
        return (StringUtils.isNotBlank(from) && !from.equals(to)) ? from : to;
    }

    public Integer copy(Integer from, Integer to) {
        return (ObjectUtils.isNotEmpty(from) && !from.equals(to)) ? from : to;
    }

    public BigDecimal copy(BigDecimal from, BigDecimal to) {
        return (ObjectUtils.isNotEmpty(from) && !from.equals(to)) ? from : to;
    }

    public <O> O copy(O from, O to) {
        return (ObjectUtils.isNotEmpty(from) && !from.equals(to)) ? from : to;
    }
}
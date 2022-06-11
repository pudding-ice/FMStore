package com.myjava.core.pojo.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest<T> implements Serializable {
    private Integer current;

    private Integer pageSize;

    private T queryContent;

    public PageRequest() {

    }

    public PageRequest(Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
    }

    public PageRequest(Integer current, Integer pageSize, T queryContent) {
        this.current = current;
        this.pageSize = pageSize;
        this.queryContent = queryContent;
    }
}

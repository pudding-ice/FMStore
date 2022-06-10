package com.myjava.core.pojo.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class PageRequest implements Serializable {
    private Integer current;

    private Integer pageSize;

    private String queryText;

    public PageRequest() {

    }

    public PageRequest(Integer current, Integer pageSize) {
        this.current = current;
        this.pageSize = pageSize;
    }
}

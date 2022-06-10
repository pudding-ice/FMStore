package com.myjava.core.pojo.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PageResponse<T> implements Serializable {
    private Long total;
    private List<T> rows;

    public PageResponse() {

    }

    public PageResponse(Long total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }
}

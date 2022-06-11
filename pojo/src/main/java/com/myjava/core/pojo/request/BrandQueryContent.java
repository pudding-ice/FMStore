package com.myjava.core.pojo.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandQueryContent implements Serializable {
    /**
     * 查询品牌名字
     */
    private String name;
    /**
     * 查询品牌首字母
     */
    private String firstChar;
    /**
     * 查询排序方式,默认是首字母升序排列
     */
    private String order = "first_char asc";

    public BrandQueryContent() {

    }

    public BrandQueryContent(String name, String firstChar) {
        this.name = name;
        this.firstChar = firstChar;
    }
}

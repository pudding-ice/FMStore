package com.myjava.core.pojo.request;

import lombok.Data;

import java.io.Serializable;


@Data
public class SellerQueryContent implements Serializable {
    private String name;

    private String nickName;
}

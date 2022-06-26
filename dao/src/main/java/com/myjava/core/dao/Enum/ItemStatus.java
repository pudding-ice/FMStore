package com.myjava.core.dao.Enum;

import com.myjava.core.dao.Enum.Base.BaseEnum;

public enum ItemStatus implements BaseEnum {
    /**
     * 第一位表示状态码,第二个表示具体信息
     * 商品状态，1-正常，2-下架，3-删除
     */
    NORMAL("1", "正常"),
    SOLD_OUT("2", "下架"),
    DELETE("3", "已删除");

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String status;

    private ItemStatus(String code, String status) {
        this.code = code;
        this.status = status;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getStatus() {
        return this.status;
    }
}

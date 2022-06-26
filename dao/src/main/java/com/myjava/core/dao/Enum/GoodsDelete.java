package com.myjava.core.dao.Enum;

import com.myjava.core.dao.Enum.Base.BaseEnum;

public enum GoodsDelete implements BaseEnum {
    /**
     * 第一位表示状态码,第二个表示具体信息
     */
    NORMAL("0", "正常"),
    DELETE("1", "已删除");

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String status;

    private GoodsDelete(String code, String status) {
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

package com.myjava.core.pojo.Enum;

import com.myjava.core.pojo.Enum.Base.BaseEnum;

public enum ContentStatus implements BaseEnum {
    /**
     * 第一位表示状态码,第二个表示具体信息
     */
    DISABLE("0", "禁用"),
    AVAILABLE("1", "可用");

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String status;

    private ContentStatus(String code, String status) {
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

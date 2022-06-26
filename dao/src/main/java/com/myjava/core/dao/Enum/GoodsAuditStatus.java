package com.myjava.core.dao.Enum;

import com.myjava.core.dao.Enum.Base.BaseEnum;

public enum GoodsAuditStatus implements BaseEnum {
    /**
     * 第一位表示状态码,第二个表示具体信息
     */
    NOT_APPLY("0", "未申请"),
    AUDITING("1", "申请中"),
    ACCEPT("2", "审核通过"),
    REJECT("3", "已驳回");

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String status;

    private GoodsAuditStatus(String code, String status) {
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

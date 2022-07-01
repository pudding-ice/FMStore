package com.myjava.core.pojo.Enum;

import com.myjava.core.pojo.Enum.Base.BaseEnum;

public enum SellerStatus implements BaseEnum {
    /**
     * 第一位表示状态码,第二个表示具体信息
     * 商家状态，0-未审核，1-审核通过，2-关闭
     */
    NOT_APPLY("0", "未审核"),
    ACCEPT("1", "审核通过"),
    REJECT("2", "审核不通过"),
    CLOSE("3", "关闭商家");

    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String status;


    private SellerStatus(String code, String status) {
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

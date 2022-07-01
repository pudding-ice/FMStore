package com.myjava.core.pojo.Enum.Base;


public interface BaseEnum {

    /**
     * 代码
     */
    String code = null;
    /**
     * 名称
     */
    String status = null;

    /**
     * 获取代码
     *
     * @return
     */
    String getCode();

    /**
     * 获取名称
     *
     * @return
     */
    String getStatus();
}

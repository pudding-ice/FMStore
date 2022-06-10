package com.myjava.core.pojo.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultMessage implements Serializable {
    private Boolean success = false;

    private String message = null;

    public ResultMessage() {

    }

    public ResultMessage(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}

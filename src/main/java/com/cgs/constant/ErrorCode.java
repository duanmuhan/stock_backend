package com.cgs.constant;

public enum ErrorCode {

    OK(200,"ok"),
    EXCEPTION(600001,"exception");
    private Integer code;
    private String message;

    ErrorCode(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

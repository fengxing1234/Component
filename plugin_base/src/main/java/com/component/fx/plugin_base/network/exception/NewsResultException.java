package com.component.fx.plugin_base.network.exception;

public class NewsResultException extends RuntimeException {


    private String resultcode;
    private String reason;
    private int error_code;

    public NewsResultException(String reason, int error_code, String resultCode) {
        super(reason);
        this.error_code = error_code;
        this.reason = reason;
        this.resultcode = resultCode;
    }
}

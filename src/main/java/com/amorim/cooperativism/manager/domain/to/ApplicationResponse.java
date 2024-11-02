package com.amorim.cooperativism.manager.domain.to;

import java.util.Date;

public class ApplicationResponse {
    private String message;
    private Integer code;
    private Date datetime;

    public ApplicationResponse(String message, Integer code) {
        this.message = message;
        this.code = code;
        this.datetime = new Date();
    }

    public ApplicationResponse(String message, Integer code, Date datetime) {
        this.message = message;
        this.code = code;
        this.datetime = datetime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }
}

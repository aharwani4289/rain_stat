package com.ajayh.rainstats.network;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Ajay.Harwani
 */

public class APIResponse implements Serializable {

    private boolean success;
    private Object data;
    private Error error;

    public APIResponse() {
        super();
    }

    public APIResponse(boolean success, Map<String, Object> data, Error error) {
        super();
        this.success = success;
        this.data = data;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public static class Error implements Serializable {
        Integer code;
        String message;

        public Error() {
            super();
        }

        public Error(Integer code, String message) {
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
}


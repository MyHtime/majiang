package cn.tecnpan.majiang.helloworld.exception;

import cn.tecnpan.majiang.helloworld.code.CustomizeErrorCode;

public class CustomizeException extends RuntimeException {

    private String message;

    public CustomizeException(CustomizeErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public CustomizeException(String message) {
        this.message = message;
    }
}

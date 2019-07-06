package cn.tecnpan.majiang.helloworld.code.impl;

import cn.tecnpan.majiang.helloworld.code.CustomizeErrorCode;

public enum CustomizeErrorCodeImpl implements CustomizeErrorCode {
    QUESTION_NOT_FOUND("你找的问题不存在，换一个试一下？");

    private String message;

    CustomizeErrorCodeImpl(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

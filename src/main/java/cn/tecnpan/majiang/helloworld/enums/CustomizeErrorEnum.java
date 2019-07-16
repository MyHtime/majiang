package cn.tecnpan.majiang.helloworld.enums;

import cn.tecnpan.majiang.helloworld.code.CustomizeErrorCode;

public enum CustomizeErrorEnum implements CustomizeErrorCode {
    QUESTION_NOT_FOUND(2001, "你找的问题不存在，换一个试一下？"),
    TARGET_PARAM_NOT_FOUND(2002, "未选中任何问题或评论进行回复！"),
    USER_NOT_LOGIN(2003, "当前操作需要登录，请登录后重试！"),
    SYSTEM_ERROR(2004, "服务GG，请等待！"),
    TYPE_PARAM_WRONG(2005, "评论类型错误或不存在！"),
    COMMENT_NOT_FOUND(2006, "你操作的评论不存在！"),
    CONTENT_IS_EMPTY(2007, "输入内容不能为空！"),
    READ_NOTIFICATION_FAIL(2008, "当前登录的用户不是该通知的接收者！"),
    NOTIFICATION_NOT_FOUND(2009, "消息不存在！"),
    FILE_UPLOAD_FAIL(2010, "上传失败！"),
    ;

    private Integer code;
    private String message;

    CustomizeErrorEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

package cn.tecnpan.majiang.helloworld.dto;

import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ResultDto {
    private Integer code;
    private String message;

    public static ResultDto errorOf(Integer code, String message) {
        ResultDto resultDto = new ResultDto();
        resultDto.setCode(code).setMessage(message);
        return resultDto;
    }

    public static ResultDto errorOf(CustomizeErrorEnum errorEnum) {
        return errorOf(errorEnum.getCode(), errorEnum.getMessage());
    }

    public static ResultDto errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(), ex.getMessage());
    }

    public static ResultDto success() {
        return new ResultDto().setCode(200).setMessage("请求成功！");
    }
}

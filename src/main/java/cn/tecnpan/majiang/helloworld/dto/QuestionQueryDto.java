package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class QuestionQueryDto {
    private String search;
    private Integer offset;
    private Integer pageSize;
}

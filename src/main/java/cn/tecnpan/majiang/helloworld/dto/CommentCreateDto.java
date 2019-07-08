package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentCreateDto {

    private Long parentId;
    private Integer type;
    private String content;
}

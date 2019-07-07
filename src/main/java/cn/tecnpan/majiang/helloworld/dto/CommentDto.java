package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentDto {

    private Integer parentId;
    private Integer type;
    private String content;
}

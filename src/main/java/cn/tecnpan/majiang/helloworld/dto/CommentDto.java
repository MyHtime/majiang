package cn.tecnpan.majiang.helloworld.dto;

import cn.tecnpan.majiang.helloworld.model.Comment;
import cn.tecnpan.majiang.helloworld.model.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CommentDto extends Comment {

    private User user;
}

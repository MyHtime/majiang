package cn.tecnpan.majiang.helloworld.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Question {
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 描述
     */
    private String description;
    /**
     * 标签
     */
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    /**
     * 发布者
     */
    private Integer creator;
    /**
     * 阅读数
     */
    private Integer viewCount;
    /**
     * 评论数
     */
    private Integer commentCount;
    /**
     * 评论数
     */
    private Integer likeCount;
}

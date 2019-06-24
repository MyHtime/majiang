package cn.tecnpan.majiang.helloworld.model;

import lombok.Data;

@Data
public class User {

    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    /**
     * 图像地址
     */
    private String avatarUrl;

}

package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * GET https://api.github.com/user
 */
@Getter
@Setter
public class GitHubUserDto {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}

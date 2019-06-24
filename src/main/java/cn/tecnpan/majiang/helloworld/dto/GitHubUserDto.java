package cn.tecnpan.majiang.helloworld.dto;

import lombok.Data;

/**
 * GET https://api.github.com/user
 */
@Data
public class GitHubUserDto {
    private String name;
    private Long id;
    private String bio;
    private String avatar_url;
}

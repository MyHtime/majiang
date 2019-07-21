package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.AccessTokenDto;
import cn.tecnpan.majiang.helloworld.dto.AuthConfigDto;
import cn.tecnpan.majiang.helloworld.dto.GitHubUserDto;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.provider.GitHubProvider;
import cn.tecnpan.majiang.helloworld.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private AuthConfigDto authConfigDto;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletResponse response) {
        AccessTokenDto accessTokenDto = new AccessTokenDto();
        accessTokenDto.setClient_id(authConfigDto.getGithubClientId());
        accessTokenDto.setClient_secret(accessTokenDto.getClient_secret());
        accessTokenDto.setCode(code);
        accessTokenDto.setRedirect_uri(authConfigDto.getGithubRedirectUri());
        accessTokenDto.setClient_secret(authConfigDto.getGithubClientSecret());
        accessTokenDto.setState(state);
        String accessToken = gitHubProvider.getAccessToken(accessTokenDto);
        GitHubUserDto gitHubUserDto = gitHubProvider.getGitHubUser(accessToken);
        if (gitHubUserDto != null && gitHubUserDto.getId() != null) {
            User user = new User();
            user.setAccountId(String.valueOf(gitHubUserDto.getId()));
            user.setName(gitHubUserDto.getName());
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setAvatarUrl(gitHubUserDto.getAvatar_url());
            //登录账号存在更新，不存在创建
            userService.createOrUpdate(user);
            // 登录成功，Cookie
            response.addCookie(new Cookie("token", token));

            return "redirect:/";
        } else {
            // 登录失败，重新登录
            log.error("callback get github error, {}", gitHubUserDto);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("loginUser");
        request.getSession().removeAttribute("unreadCount");
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}

package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.AccessToken;
import cn.tecnpan.majiang.helloworld.dto.GitHubUser;
import cn.tecnpan.majiang.helloworld.model.User;
import cn.tecnpan.majiang.helloworld.provider.GitHubProvider;
import cn.tecnpan.majiang.helloworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state, HttpServletRequest request) {
        AccessToken accessToken = new AccessToken();
        accessToken.setClient_id(clientId);
        accessToken.setClient_secret(clientSecret);
        accessToken.setCode(code);
        accessToken.setRedirect_uri(redirectUri);
        accessToken.setState(state);
        String token = gitHubProvider.getAccessToken(accessToken);
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(token);
        if (gitHubUser != null) {
            // 登录成功，写Session,Cookie
            request.getSession().setAttribute("loginUser", gitHubUser);
            User user = new User();
            user.setAccountId(String.valueOf(gitHubUser.getId()));
            user.setName(gitHubUser.getName());
            user.setToken(UUID.randomUUID().toString());
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModify(user.getGmtCreate());
            userService.insertUser(user);
            return "redirect:/";
        } else {
            // 登录失败，重新登录
            return "redirect:/";
        }
    }
}

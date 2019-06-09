package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.AccessToken;
import cn.tecnpan.majiang.helloworld.dto.GitHubUser;
import cn.tecnpan.majiang.helloworld.provider.GitHubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @GetMapping("/callback")
    public String callback(@RequestParam("code") String code, @RequestParam("state") String state) {
        AccessToken accessToken = new AccessToken();
        accessToken.setCode(code);
        accessToken.setRedirect_uri("http://localhost:8887/callback");
        accessToken.setState(state);
        accessToken.setClient_id("6ec56ffc2179dd5c6116");
        accessToken.setClient_secret("68bee3acbd3d4602ad1950e4cc813876e81f87cf");
        String token = gitHubProvider.getAccessToken(accessToken);
        GitHubUser gitHubUser = gitHubProvider.getGitHubUser(token);
        System.out.println(gitHubUser.getName());
        return "index";
    }
}

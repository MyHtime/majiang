package cn.tecnpan.majiang.helloworld.provider;

import cn.tecnpan.majiang.helloworld.dto.AccessToken;
import cn.tecnpan.majiang.helloworld.dto.GitHubUser;
import com.alibaba.fastjson.JSON;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 1. @Controller 当前类作为路由API的承载者
 * 2. @Component 把当前类初始化到Spring容器的上下文
 */
@Component
public class GitHubProvider {

    /**
     * 2. Users are redirected back to your site by GitHub
     * POST https://github.com/login/oauth/access_token
     * @param accessToken accessToken
     * @return String
     */
    public String getAccessToken(AccessToken accessToken) {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessToken));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return string.split("&")[0].split("=")[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 3. Use the access token to access the API
     * Authorization: token OAUTH-TOKEN
     * GET https://api.github.com/user
     * @param accessToken accessToken
     * @return GitHubUser
     */
    public GitHubUser getGitHubUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            return JSON.parseObject(string, GitHubUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

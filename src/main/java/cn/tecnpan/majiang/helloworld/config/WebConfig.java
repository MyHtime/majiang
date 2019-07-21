package cn.tecnpan.majiang.helloworld.config;

import cn.tecnpan.majiang.helloworld.dto.AuthConfigDto;
import cn.tecnpan.majiang.helloworld.interceptor.SessionInterceptor;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.ObjectRemoteAuthorization;
import cn.ucloud.ufile.auth.UfileObjectRemoteAuthorization;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Slf4j
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Value("${auth.server.key}")
    private String authKey;

    @Value("${auth.server.url}")
    private String authServerUrl;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns() 拦截的路径
        //excludePathPatterns()  过滤的路径
        // /** 所有地址
        //这样造成了一个问题，静态资源文件也被拦截了：cause by @EnableWebMvc
        //@EnableWebMvc
        //@Import(DelegatingWebMvcConfiguration.class)//导入DelegatingWebMvcConfiguration
        //DelegatingWebMvcConfiguration extends WebMvcConfigurationSupport//继承了WebMvcConfigurationSupport

        //WebMvcAutoConfiguration
        //@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)//在WebMvcConfigurationSupport不存在的时候配置
        //public void addResourceHandlers(ResourceHandlerRegistry registry)//注册静态资源

        //@Autowired用在方法上，会注入方法参数列表这个类型的对象（byType）
        //see：https://docs.spring.io/spring/docs/5.1.8.RELEASE/spring-framework-reference/core.html#beans-factory-autowire
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }


    @Bean
    public AuthConfigDto getAuthConfigDto() {
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder().url(authServerUrl).post(body).build();

        OkHttpClient client = new OkHttpClient();

        try (Response response = client.newCall(request).execute()) {
            log.info("{}，获取验证服务成功", response.code());
            return JSON.parseObject(response.body().string(), AuthConfigDto.class);
        } catch (IOException e) {
            e.getMessage();
        }

        return null;
    }


    @Bean
    @ConditionalOnBean(AuthConfigDto.class)
    public ObjectAuthorization getObjectRemoteAuthorization(AuthConfigDto authConfigDto) {
        return new UfileObjectRemoteAuthorization(authConfigDto.getUFilePublicKey(),
                new ObjectRemoteAuthorization.ApiConfig(authConfigDto.getUFileApplyAuth(),
                        authConfigDto.getUFileApplyPrivateUrlAuth()));
    }

    @Bean
    @ConditionalOnBean(AuthConfigDto.class)
    public ObjectConfig getObjectConfig(AuthConfigDto authConfigDto) {
        return new ObjectConfig(authConfigDto.getUFileRegion(), authConfigDto.getUFileProxySuffix());
    }


    @Bean
    public OkHttpClient getOkHttpClient() {
        return new OkHttpClient();
    }
}

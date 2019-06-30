package cn.tecnpan.majiang.helloworld.config;

import cn.tecnpan.majiang.helloworld.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //addPathPatterns() 拦截的路径
        //excludePathPatterns()  过滤的路径
        // /** 所有地址
        registry.addInterceptor(sessionInterceptor).addPathPatterns("/**");
    }
}

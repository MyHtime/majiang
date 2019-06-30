package cn.tecnpan.majiang.helloworld.config;

import cn.tecnpan.majiang.helloworld.interceptor.SessionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SessionInterceptor sessionInterceptor;

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
}

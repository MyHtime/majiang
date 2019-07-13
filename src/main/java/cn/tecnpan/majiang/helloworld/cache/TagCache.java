package cn.tecnpan.majiang.helloworld.cache;

import cn.tecnpan.majiang.helloworld.dto.TagDto;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TagCache {
    public static List<TagDto> get() {
        List<TagDto> tagDtoList = new ArrayList<>();
        //开发语言
        tagDtoList.add(new TagDto().setCategoryName("开发语言").setTags(Arrays.asList("javascript", "php", "css", "html", "html5", "java", "node.js", "python", "c++", "c", "golang", "objective-c", "typescript", "shell", "swift", "c#", "sass", "ruby", "bash", "less", "asp.net", "lua", "scala", "coffeescript", "actionscript", "rust", "erlang", "perl")));
        //平台框架
        tagDtoList.add(new TagDto().setCategoryName("平台框架").setTags(Arrays.asList("laravel", "spring", "express", "django", "flask", "yii", "ruby-on-rails", "tornado", "koa", "struts")));
        //服务器
        tagDtoList.add(new TagDto().setCategoryName("服务器").setTags(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos", "缓存 tomcat", "负载均衡", "unix", "hadoop", "windows-server")));
        //数据库
        tagDtoList.add(new TagDto().setCategoryName("数据库").setTags(Arrays.asList("mysql", "redis", "mongodb", "sql", "oracle", "nosql memcached", "sqlserver", "postgresql", "sqlite")));
        //开发工具
        tagDtoList.add(new TagDto().setCategoryName("开发工具").setTags(Arrays.asList("git", "github", "visual-studio-code", "vim", "sublime-text", "xcode intellij-idea", "eclipse", "maven", "ide", "svn", "visual-studio", "atom emacs", "textmate", "hg")));
        return tagDtoList;
    }

    public static String filterInvalidTags(String tags) {
        String[] tag = StringUtils.split(tags, ",");
        List<TagDto> tagDtos = get();
        List<String> tagList = tagDtos.stream().flatMap(tagDto -> tagDto.getTags().stream()).collect(Collectors.toList());
        return Arrays.stream(tag).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
    }
}

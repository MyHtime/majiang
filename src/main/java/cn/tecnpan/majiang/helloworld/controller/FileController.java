package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.FileDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FileController {

    /**
     * markdown图片上传
     */
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDto upload() {
        return new FileDto().setSuccess(1).setUrl("/img/wallpaper01.jpg");
    }
}

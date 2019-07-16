package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.FileDto;
import cn.tecnpan.majiang.helloworld.provider.UCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    /**
     * markdown图片上传
     */
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDto upload(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
        String fileName = "";
        try {
            fileName = uCloudProvider.upload(multipartFile.getInputStream(), multipartFile.getContentType(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        System.out.println(fileName);
        return new FileDto().setSuccess(1).setUrl("/img/wallpaper01.jpg");
    }
}

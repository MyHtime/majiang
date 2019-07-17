package cn.tecnpan.majiang.helloworld.controller;

import cn.tecnpan.majiang.helloworld.dto.FileDto;
import cn.tecnpan.majiang.helloworld.provider.UCloudProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@Slf4j
public class FileController {

    @Autowired
    private UCloudProvider uCloudProvider;

    /**
     * markdown图片上传
     */
    @ResponseBody
    @RequestMapping("/file/upload")
    public FileDto upload(@RequestParam("editormd-image-file") MultipartFile multipartFile) {
        String url = "";
        try {
            url = uCloudProvider.upload(multipartFile.getInputStream(), multipartFile.getContentType(), Objects.requireNonNull(multipartFile.getOriginalFilename()));
            return new FileDto().setSuccess(1).setUrl(url).setMessage("上传成功");
        } catch (IOException | NullPointerException e) {
            log.error("upload error", e);
            return new FileDto().setSuccess(0).setMessage("上传失败");
        }
    }
}

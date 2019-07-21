package cn.tecnpan.majiang.helloworld.provider;

import cn.tecnpan.majiang.helloworld.dto.AuthConfigDto;
import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

/**
 * UCloudProvider
 * https://www.ucloud.cn/
 */
@Component
@Slf4j
public class UCloudProvider {

    @Autowired
    private AuthConfigDto authConfigDto;

    @Autowired
    private ObjectAuthorization objectAuthorization;

    @Autowired
    private ObjectConfig objectConfig;

    /**
     * 有效时限
     */
    @Value("${ucloud.ufile.expires-duration}")
    private int expiresDuration;

    /**
     *
     * @param inputStream 输入流
     * @param mimeType 文件类型
     * @param fileName 文件名
     * @return 图片名字
     */
    public String upload(InputStream inputStream, String mimeType, String fileName) {
        String generatedFileName = "";
        String[] fileSplit = fileName.split("\\.");
        if (fileSplit.length > 1) {
            generatedFileName = UUID.randomUUID().toString() + "." + fileSplit[fileSplit.length - 1];
        } else {
            throw new CustomizeException(CustomizeErrorEnum.FILE_UPLOAD_FAIL);
        }

        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorization, objectConfig)
                    .putObject(inputStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(authConfigDto.getUFileBucketName())
                    /**
                     * 是否上传校验MD5, Default = true
                     */
                    //  .withVerifyMd5(false)
                    /**
                     * 指定progress callback的间隔, Default = 每秒回调
                     */
                    //  .withProgressConfig(ProgressConfig.callbackWithPercent(10))
                    /**
                     * 配置进度监听
                     */
                    .setOnProgressListener((bytesWritten, contentLength) -> {

                    })
                    .execute();
            if (response != null && response.getRetCode() == 0) {
                return UfileClient.object(objectAuthorization, objectConfig)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, authConfigDto.getUFileBucketName(), expiresDuration)
                        .createUrl();
            } else {
                log.error("upload error,{}", response);
                throw new CustomizeException(CustomizeErrorEnum.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException | UfileServerException e) {
            log.error("upload error,{}", fileName, e);
            throw new CustomizeException(CustomizeErrorEnum.FILE_UPLOAD_FAIL);
        }
    }


}

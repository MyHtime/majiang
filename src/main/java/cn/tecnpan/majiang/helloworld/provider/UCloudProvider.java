package cn.tecnpan.majiang.helloworld.provider;

import cn.tecnpan.majiang.helloworld.enums.CustomizeErrorEnum;
import cn.tecnpan.majiang.helloworld.exception.CustomizeException;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.ObjectRemoteAuthorization;
import cn.ucloud.ufile.auth.UfileObjectRemoteAuthorization;
import cn.ucloud.ufile.bean.PutObjectResultBean;
import cn.ucloud.ufile.exception.UfileClientException;
import cn.ucloud.ufile.exception.UfileServerException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.UUID;

/**
 * UCloudProvider
 * https://www.ucloud.cn/
 */
@Component
public class UCloudProvider {

    /**
     * 公钥
     */
    @Value("${ucloud.ufile.public-key}")
    private String publicKey;

    /**
     * 验证地址
     */
    @Value("${ucloud.ufile.apply.auth}")
    private String applyAuth;

    /**
     * 验证地址
     */
    @Value("${ucloud.ufile.apply.private-url-auth}")
    private String applyPrivateUrlAuth;

    /**
     * 空间名
     */
    @Value("${ucloud.ufile.bucket-name}")
    private String bucketName;

    /**
     * 有效时限
     */
    @Value("${ucloud.ufile.expires-duration}")
    private int expiresDuration;

    /**
     * 仓库地区
     */
    @Value("${ucloud.ufile.region}")
    private String region;

    /**
     * 代理后缀
     */
    @Value("${ucloud.ufile.proxy-suffix}")
    private String proxySuffix;

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

        ObjectAuthorization objectAuthorizer = new UfileObjectRemoteAuthorization(publicKey, new ObjectRemoteAuthorization.ApiConfig(applyAuth, applyPrivateUrlAuth));
        ObjectConfig config = new ObjectConfig(region, proxySuffix);
        try {
            PutObjectResultBean response = UfileClient.object(objectAuthorizer, config)
                    .putObject(inputStream, mimeType)
                    .nameAs(generatedFileName)
                    .toBucket(bucketName)
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
                return UfileClient.object(objectAuthorizer, config)
                        .getDownloadUrlFromPrivateBucket(generatedFileName, bucketName, expiresDuration)
                        .createUrl();
            } else {
                throw new CustomizeException(CustomizeErrorEnum.FILE_UPLOAD_FAIL);
            }
        } catch (UfileClientException | UfileServerException e) {
            e.printStackTrace();
            throw new CustomizeException(CustomizeErrorEnum.FILE_UPLOAD_FAIL);
        }
    }


}

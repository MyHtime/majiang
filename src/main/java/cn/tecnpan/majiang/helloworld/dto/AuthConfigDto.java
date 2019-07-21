package cn.tecnpan.majiang.helloworld.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AuthConfigDto {
    /**
     * github
     */
    private String githubClientId;
    /**
     * github
     */
    private String githubClientSecret;
    /**
     * 重定向
     */
    private String githubRedirectUri;
    /**
     * 公钥
     */
    private String uFilePublicKey;
    /**
     * 验证地址
     */
    private String uFileApplyAuth;
    /**
     * 验证地址
     */
    private String uFileApplyPrivateUrlAuth;
    /**
     * 空间名
     */
    private String uFileBucketName;

    /**
     *
     */
    private String uFileRegion;

    /**
     *
     */
    private String uFileProxySuffix;
}

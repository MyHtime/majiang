## from Ma Jiang Community

## 资料
[Spring 文档](https://spring.io/guides)<br>
[Spring Web 文档](https://spring.io/guides/gs/serving-web-content/)<br>
[ES 社区](https://elasticsearch.cn/explore)<br>
[GitHub deploy key](https://developer.github.com/v3/guides/managing-deploy-keys/#deploy-keys)<br>
[Bootstrap 文档](https://v3.bootcss.com/)<br>
[GitHub OAuth - 1](https://developer.github.com/apps/building-oauth-apps/creating-an-oauth-app/)<br>
[GitHub OAuth - 2](https://developer.github.com/apps/building-oauth-apps/authorizing-oauth-apps/)<br>
[OkHttp](https://square.github.io/okhttp/)<br>
[SpringBoot DataSource](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#boot-features-embedded-database-support)<br>
[mybatis-spring-boot-autoconfigure](http://www.mybatis.org/spring-boot-starter/mybatis-spring-boot-autoconfigure/)<br>
[ufile-sdk-java](https://github.com/ucloud/ufile-sdk-java)  

## 工具
[Git](https://git-scm.com/download)<br>
[Visual Paradigm](https://www.visual-paradigm.com)<br>
[flyway](https://flywaydb.org/)  
[lombok](https://www.projectlombok.org/)  
springboot热部署：[Spring Dev Tool](https://docs.spring.io/spring-boot/docs/2.0.0.RC1/reference/htmlsingle/#using-boot-devtools)  
Google插件：[Live Reload](https://chrome.google.com/webstore/detail/livereload/jnihajbhpnppcggbcgedagnkighmdlei/related)  
[markDown](https://pandao.github.io/editor.md/index.html)  
## 脚本
```sql
CREATE TABLE USER
(
    ID int AUTO_INCREMENT PRIMARY KEY NOT NULL,
    ACCOUNT_ID VARCHAR(100),
    NAME VARCHAR(50),
    TOKEN VARCHAR(36),
    GMT_CREATE BIGINT,
    GMT_MODIFIED BIGINT
);
```
```
mvn flyway:migrate
mvn -Dmybatis.generator.overwrite=true mybatis-generator:generate
```
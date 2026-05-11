# ERP系统启用oauth2 server步骤

### 1.在mxpio-erp-parent中引入此模块

```xml
<dependency>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-boot-module-oauth2-server</artifactId>
</dependency>
```

### 2.启动项目后，项目会在数据库中建表MB_OAUTH_CLIENT_DETAILS,在此表中维护接入端内容
##### 2.1 client secret的格式：
      2.1.1 明文密码
            {noop}123456
      2.1.2 bcrypt加密格式
            {bcrypt}$2a$10$.c/TiWuSmvwitqxBZsF5guQ6qQn08E1b1Aldff/LEb.dkeR0b4RGq

### 3.第三方平台调用前端地址/oauth,获取code码
      比如：前端地址为http://127.0.0.1:9007,则获取code地址码链接为：http://localhost:9008/oauth/authorize?response_type=code&client_id=xxxxx&redirect_uri=http://xxx.xxx.xxx.xxx&scope=read&state=xxxxx
      如果成功，则系统会将获取的code码附加在redirect_uri提供的地址后面返回，注意，此请求为get

### 4、第三方平台获取到code码后，调用后端地址的接口，用code获取token
      比如后端地址为http://127.0.0.1:9008,则调用地址为
       http://127.0.0.1:9008/oauth/token?grant_type=authorization_code&client_id=fangsong&client_secret=123456&redirect_uri=http://www.baidu.com&code=4amfMp
     
### 5、将获取的token放在header的Access-Token中，访问erp系统


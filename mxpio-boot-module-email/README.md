**调用模块的yml配置中需要引入以下**

```java
spring:
  mail:
    host: smtp.163.com
    username: ***@163.com
    password: EFRBOX*****
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```


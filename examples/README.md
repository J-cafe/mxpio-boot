# Examples

示例工程目录，演示如何基于 mxpio-boot 框架构建应用。此目录不是 Maven 模块，每个示例需独立构建运行。

## mxpio-boot-example

基础示例工程，展示了框架的完整启动配置。

```bash
# 1. 先安装框架到本地仓库
cd mxpio-boot
mvn clean install -DskipTests

# 2. 启动示例工程
cd examples/mxpio-boot-example
# 编辑 src/main/resources/application-dev.yml，修改数据库连接信息
mvn spring-boot:run
```

启动后访问：
- 应用：`http://localhost:9005`
- API 文档：`http://localhost:9005/swagger-ui.html`
- 默认账户：`admin` / `123456`

## 基于此框架新建项目

在 `examples/` 下新建目录，pom.xml 继承 `mxpio-boot-parent`，然后添加所需的模块依赖即可：

```xml
<parent>
    <groupId>com.mxpio</groupId>
    <artifactId>mxpio-boot-parent</artifactId>
    <version>4.0.0-beta.1</version>
    <relativePath>../../pom.xml</relativePath>
</parent>

<dependencies>
    <dependency>
        <groupId>com.mxpio</groupId>
        <artifactId>mxpio-autoconfigure</artifactId>
    </dependency>
    <!-- 按需添加其他模块 -->
</dependencies>
```

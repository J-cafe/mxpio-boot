# mxpio-boot-base-security
## 简介
基于spring security，以角色为中心的权限管理功能。权限粒度为组件级别。主要对页面和页面中的组件进行权限控制。如需对其他资源进行权限控制，需要简单实现相关接口即可--SecurityMetadataSource。可被授权的对象也是可以扩展的，也十分简单，实现一个接口即可--GrantedAuthorityProvider。
## 机制
当一个资源没有分配给任何角色的时候，这个资源就是公开的，任何人都可以访问。否则，只有有权限的人才能访问。授权过程包含三类对象：演员（Actor）、角色（Role）和资源（Resource）。演员可以是用户、部门和岗位等；资源可以是菜单和组件等。演员和资源可以任意扩展。
### 授权过程：
1. 资源分配给角色（权限信息）
2. 角色授权给演员（授权信息）
### 权限决策：
1. 获取演员的授权信息
2. 获取资源的权限信息
3. 角色授权信息和权限信息匹配是否成功
### 扩展演员
实现GrantedAuthorityProvider接口，提供演员的授权信息。
### 扩展资源
实现SecurityMetadataSource接口，提供资源的权限信息。
## 权限决策管理器SecurityDecisionManager
提供一个统一的权限决策接口。接口定义如下：
```java
/**
 * 权限决策管理器。<br>
 * 实现{@link org.springframework.security.access.SecurityMetadataSource}来<br>
 * 提供自己定义资源的权限信息，就可以实现权限决策判断
 * @author Kevin Yang (mailto:muxiangqiu@gmail.com)
 * @since 2016年7月7日
 */
public interface SecurityDecisionManager {

	/**
	 * 决策给定的资源，当前登陆用户是否有权限
	 * @param resource 资源
	 * @return 结果。true为有权限，否则没有
	 */
	boolean decide(Resource resource);

	/**
	 * 根据资源获取权限信息
	 * @param resource 资源
	 * @return 权限列表
	 */
	Collection<ConfigAttribute> findConfigAttributes(Resource resource);

	/**
	 * 决策给定的资源，给定用户是否有权限
	 * @param username 用户名
	 * @param resource 资源
	 * @return 结果。true为有权限，否则没有
	 */
	boolean decide(String username, Resource resource);
}

```
### 属性配置及默认值
```java
#应用名称
panda.appName=Bstek Development Framework
#登陆页面背景图片
panda.loginImageBg=dorado/res/static/images/login-bg.jpg
#首次启动时，用户表为空的情况下是否自动创建一个默认的用户：admin/123456
panda.autoCreateIfUserIsEmpty=true
#登陆页面地址
panda.loginPath=panda.security.ui.view.Login.d
#登出地址
panda.logoutPath=logout
#系统需要匿名访问URL
panda.systemAnonymous=/static/**,/**/*.dpkg,/dorado/client/**,/dorado/res/**,/**/*.js, /**/*.css
#用户自定义需要匿名访问的URL
panda.customAnonymous=
#欢迎页面的标题
panda.welcomeTitle=\u6B22\u8FCE
#欢迎页面的图片
panda.welcomeIcon=fa fa-university blue-text
#欢迎页面的地址
panda.welcomePath=
#个人中心页面的地址
panda.personCenter=panda.security.ui.view.PersonalCenter.d
```


package com.mxpioframework.oauth.server.web;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * OAuth2 授权服务器的登录页面。
 *
 * <p>mxpio-security 原有的 /login 为无状态接口（返回 JSON），不适合浏览器授权码流程，
 * 故在 oauth2-server 模块内提供独立的 /oauth2/login 表单登录页。该页面仅用于授权码流程中
 * 资源所有者的登录，登录认证复用 mxpio 的 UserDetailsService + PasswordEncoder（不改动 mxpio-security）。
 * 表单提交（POST /oauth2/login）由 Spring Security 的 UsernamePasswordAuthenticationFilter 处理。
 */
@Controller
public class OAuth2LoginController {

	@GetMapping("/oauth2/login")
	@ResponseBody
	public String login(HttpServletRequest request,
			@RequestParam(value = "error", required = false) String error) {
		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

		StringBuilder html = new StringBuilder();
		html.append("<!DOCTYPE html><html lang=\"zh\"><head><meta charset=\"UTF-8\">");
		html.append("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		html.append("<title>OAuth2 授权登录</title>");
		html.append("<style>");
		html.append("body{font-family:sans-serif;max-width:340px;margin:80px auto;color:#333}");
		html.append(".err{color:#d9534f;margin:0 0 12px}");
		html.append(".lab{display:block;margin:12px 0 4px}");
		html.append("input[type=text],input[type=password]{width:100%;padding:8px;box-sizing:border-box}");
		html.append("button{margin-top:16px;width:100%;padding:9px;background:#1890ff;color:#fff;border:none;border-radius:4px;cursor:pointer}");
		html.append("</style></head><body>");
		html.append("<h2>OAuth2 授权登录</h2>");
		if (error != null) {
			html.append("<p class=\"err\">用户名或密码错误，请重试。</p>");
		}
		html.append("<form action=\"/oauth2/login\" method=\"post\">");
		if (csrf != null) {
			html.append("<input type=\"hidden\" name=\"").append(csrf.getParameterName())
					.append("\" value=\"").append(csrf.getToken()).append("\"/>");
		}
		html.append("<label class=\"lab\" for=\"username\">用户名</label>");
		html.append("<input id=\"username\" type=\"text\" name=\"username\" autocomplete=\"username\" required/>");
		html.append("<label class=\"lab\" for=\"password\">密码</label>");
		html.append("<input id=\"password\" type=\"password\" name=\"password\" autocomplete=\"current-password\" required/>");
		html.append("<button type=\"submit\">登录并授权</button>");
		html.append("</form></body></html>");
		return html.toString();
	}

}

package com.mxpioframework.oauth.controller;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @Author : wpp
 * @Datetime : 2023/10/25 17:22
 * @Desc :
 * @Modor :  Modifytime:
 * @modDesc :
 */
@Tag(name = "Auth2CallBackController", description = "Auth2CallBackController")
@RestController("mxpio.auth2.Auth2CallBackController")
@RequestMapping("/oauth/")
public class OAuthCallBackController {
    @Value("${spring.security.oauth2.client.registration.authing.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.authing.client-secret}")
    private String clientSecret;
    @Value("${spring.security.oauth2.client.registration.authing.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.provider.authing.issuer-uri}")
    private String issueUri;

    @RequestMapping("callback")
    public String getTokenByCode(HttpServletRequest req, HttpServletResponse resp, String code) throws IOException {
        /*//返回token、idtoken、Bearer 等信息
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("code",code);
        paramMap.put("client_id",clientId);
        paramMap.put("client_secret",clientSecret);
        paramMap.put("grant_type","authorization_code");
        paramMap.put("redirect_uri",redirectUri);
        String result = HttpUtil.post(issueUri+"/token", paramMap);*/
        //直接返回用户授权码
        return code;
    }
}

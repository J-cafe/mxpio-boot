package com.mxpioframework.security.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.captcha.CaptchaDTO;
import com.mxpioframework.security.captcha.impl.DefaultCaptcha;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.vo.TokenVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "SystemController", tags = { "系统接口" })
@RestController
@RequestMapping("/")
public class SystemController {

	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private DefaultCaptcha defaultCaptcha;
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	@GetMapping("captcha")
	@ApiOperation(value = "加载验证码", notes = "获取登录验证码", httpMethod = "GET")
	public Result<CaptchaDTO> captcha() throws IOException {
		CaptchaDTO captcha = new CaptchaDTO();;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        String createText = defaultCaptcha.createText();
        String uuid = UUID.randomUUID().toString();
        cacheProvider.set(Constants.CAPTCHA_REDIS_KEY + uuid, createText, 180);
        
        BufferedImage bi = defaultCaptcha.createImage(createText);
        ImageIO.write(bi, "jpg", out);

        Base64.Encoder encoder = Base64.getEncoder();
        
        captcha.setCode(uuid);
        captcha.setImage(encoder.encodeToString(out.toByteArray()));
		return Result.OK(captcha);
	}
	
	@PostMapping("token/refresh")
	@ApiOperation(value = "刷新token", notes = "双token机制下通过refreshToken刷新权限token", httpMethod = "POST")
	public Result<Object> refreshToken(@RequestBody TokenVo tokenVo) throws IOException {
		TokenVo result;
		result = onlineUserService.refreshToken(tokenVo.getRefreshToken(), cacheProvider);
		if(result != null) {
			return Result.OK(result);
		}else {
			return Result.noauth("refreshToken已失效");
		}
	}
}

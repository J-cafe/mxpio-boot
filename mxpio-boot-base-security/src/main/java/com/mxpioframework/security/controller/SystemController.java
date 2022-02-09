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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.kaptcha.KaptchaDTO;
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
	private DefaultKaptcha defaultKaptcha;
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	@GetMapping("kaptcha")
	@ApiOperation(value = "加载验证码", notes = "获取登录验证码", httpMethod = "GET")
	public Result<KaptchaDTO> kaptcha() throws IOException {
		KaptchaDTO captcha = new KaptchaDTO();;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        String createText = defaultKaptcha.createText();
        String uuid = UUID.randomUUID().toString();
        cacheProvider.set(Constants.KAPTCHA_REDIS_KEY + uuid, createText, 180);
        
        BufferedImage bi = defaultKaptcha.createImage(createText);
        ImageIO.write(bi, "jpg", out);

        Base64.Encoder encoder = Base64.getEncoder();
        
        captcha.setCode(uuid);
        captcha.setImage(encoder.encodeToString(out.toByteArray()));
		return Result.OK(captcha);
	}
	
	@PostMapping("token/refresh")
	@ApiOperation(value = "刷新token", notes = "双token机制下通过refreshToken刷新权限token", httpMethod = "POST")
	public Result<Object> refreshToken(String refreshToken) throws IOException {
		TokenVo tokenVo;
		tokenVo = onlineUserService.refreshToken(refreshToken, cacheProvider);
		if(tokenVo != null) {
			return Result.OK(tokenVo);
		}else {
			return Result.noauth("refreshToken已失效");
		}
	}
}

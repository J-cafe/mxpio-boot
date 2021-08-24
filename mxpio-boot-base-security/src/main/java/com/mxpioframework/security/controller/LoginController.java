package com.mxpioframework.security.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.Constants;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.kaptcha.KaptchaDTO;
import com.mxpioframework.security.service.UrlService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import sun.misc.BASE64Encoder;

@Api(value = "LoginController", tags = { "系统接口" })
@RestController
@RequestMapping("/")
public class LoginController {

	@Autowired
	private UrlService urlService;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private DefaultKaptcha defaultKaptcha;

	@GetMapping("/loadUrl")
	@ApiOperation(value = "加载菜单")
	public Result<List<Url>> loadUrl() {
		return Result.OK(urlService.findTreeByUsername(null));
	}
	
	@GetMapping("/kaptcha")
	@ApiOperation(value = "加载验证码")
	public Result<KaptchaDTO> kaptcha() throws IOException {
		KaptchaDTO captcha = new KaptchaDTO();;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        String createText = defaultKaptcha.createText();
        String uuid = UUID.randomUUID().toString();
        cacheProvider.set(Constants.KAPTCHA_REDIS_KEY + uuid, createText, 180);
        
        BufferedImage bi = defaultKaptcha.createImage(createText);
        ImageIO.write(bi, "jpg", out);

        BASE64Encoder encoder = new BASE64Encoder();
        
        captcha.setCode(uuid);
        captcha.setImage(encoder.encode(out.toByteArray()));
		return Result.OK(captcha);
	}
}

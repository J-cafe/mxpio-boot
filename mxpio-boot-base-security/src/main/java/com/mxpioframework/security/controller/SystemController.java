package com.mxpioframework.security.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "SystemController", description = "系统接口")
@RestController("mxpio.security.systemController")
@RequestMapping("/")
public class SystemController {

	@Autowired
	private CacheProvider cacheProvider;

	@Autowired
	private DefaultCaptcha defaultCaptcha;

	@Autowired
	private OnlineUserService onlineUserService;

	@Value("${mxpio.captcha.open}")
	private String captchaOpenFlag;
	@Value("${mxpio.password.strategy}")
	private String passwordStrategy;

	@Value("${app_name}")
	private String appName;
	@Value("${app_system_desc}")
	private String appSystemDesc;
	@Value("${app_system_abbr}")
	private String appSystemAbbr;
	@Value("${app_user_company}")
	private String appUserCompany;
	@GetMapping("captcha")
	@Operation(summary = "加载验证码", description = "获取登录验证码", method = "GET")
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
	@Operation(summary = "刷新token", description = "双token机制下通过refreshToken刷新权限token", method = "POST")
	public Result<Object> refreshToken(@RequestBody TokenVo tokenVo) throws IOException {
		TokenVo result;
		result = onlineUserService.refreshToken(tokenVo.getRefreshToken(), cacheProvider);
		if(result != null) {
			return Result.OK(result);
		}else {
			return Result.noauth403("refreshToken已失效");
		}
	}

	@GetMapping("loadConfigResource")
	@Operation(summary = "获取后端资源配置", description = "获取后端资源配置", method = "GET")
	public Result<Map<String,Object>> loadConfigResource() {
		Map<String,Object> returnMap = new HashMap<>();
		if (StringUtils.equals("true",captchaOpenFlag)){
			returnMap.put("captchaOpenFlag",Boolean.TRUE);
		}else{
			returnMap.put("captchaOpenFlag",Boolean.FALSE);
		}
		returnMap.put("appName",appName);
		returnMap.put("appSystemDesc",appSystemDesc);
		returnMap.put("appSystemAbbr",appSystemAbbr);
		returnMap.put("appUserCompany",appUserCompany);
		if (StringUtils.isNotBlank(passwordStrategy)){
			String[] split = passwordStrategy.split(",");
			List<Map<String,Object>> configRegexs = new ArrayList<>();
			for (String per:split){
				Map<String,Object> patternMap = new HashMap<>();
				switch (per){
					case "DIGANDLETTER":
						patternMap.put("pattern",Constants.RegexEnum.DIGANDLETTER.getCode());
						patternMap.put("message",Constants.RegexEnum.DIGANDLETTER.getName());
						configRegexs.add(patternMap);
						break;
					case "CONTINUOUS":
						patternMap.put("pattern",Constants.RegexEnum.CONTINUOUS.getCode());
						patternMap.put("message",Constants.RegexEnum.CONTINUOUS.getName());
						configRegexs.add(patternMap);
						break;
					case "MINLENGTH":
						patternMap.put("min",Integer.parseInt(Constants.RegexEnum.MINLENGTH.getCode()));
						patternMap.put("message",Constants.RegexEnum.MINLENGTH.getName());
						configRegexs.add(patternMap);
						break;
				}
			}
			returnMap.put("passwordStrategy",configRegexs);
		}
		return Result.OK(returnMap);
	}
}

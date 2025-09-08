package com.mxpioframework.security.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mxpioframework.common.CommonConstant;
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

	@Value("${app.name}")
	private String appName;
	@Value("${app.system.desc}")
	private String appSystemDesc;
	@Value("${app.system.abbr}")
	private String appSystemAbbr;
	@Value("${app.user.company}")
	private String appUserCompany;
	@Value("${mxpio.token.time:1800000}")
	private long tokenTime;
	@Value("${mxpio.refresh.token.time:7200000}")
	private long refreshTokenTime;
	@Value("${mxpio.logo}")
	private String logoPath;

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
	public Result<Map<String,Object>> loadConfigResource(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> returnMap = new HashMap<>();
		returnMap.put("isMultitenant", CommonConstant.isIncludeModule("Multitenant"));
		if (StringUtils.equals("true",captchaOpenFlag)){
			returnMap.put("captchaOpenFlag",Boolean.TRUE);
		}else{
			returnMap.put("captchaOpenFlag",Boolean.FALSE);
		}
		returnMap.put("appName",appName);
		returnMap.put("appSystemDesc",appSystemDesc);
		returnMap.put("appSystemAbbr",appSystemAbbr);
		returnMap.put("appUserCompany",appUserCompany);
		returnMap.put("tokenTime",tokenTime);
		returnMap.put("refreshTokenTime",refreshTokenTime);
		returnMap.put("logo",logoPath);
		if (StringUtils.isNotBlank(passwordStrategy)){
			String[] split = passwordStrategy.split(",");
			List<Map<String,Object>> configRegexs = new ArrayList<>();
			for (String per:split){
				Map<String,Object> patternMap = new HashMap<>();
				switch (per){
					case "DIGANDLETTERCASE":
						patternMap.put("pattern",Constants.RegexEnum.DIGANDLETTERCASE.getCode());
						patternMap.put("message",Constants.RegexEnum.DIGANDLETTERCASE.getName());
						configRegexs.add(patternMap);
						break;
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
					case "MINLENGTH12":
						patternMap.put("min",Integer.parseInt(Constants.RegexEnum.MINLENGTH12.getCode()));
						patternMap.put("message",Constants.RegexEnum.MINLENGTH12.getName());
						configRegexs.add(patternMap);
						break;
					case "MINLENGTH8":
						patternMap.put("min",Integer.parseInt(Constants.RegexEnum.MINLENGTH8.getCode()));
						patternMap.put("message",Constants.RegexEnum.MINLENGTH8.getName());
						configRegexs.add(patternMap);
						break;
					case "MINLENGTH6":
						patternMap.put("min",Integer.parseInt(Constants.RegexEnum.MINLENGTH6.getCode()));
						patternMap.put("message",Constants.RegexEnum.MINLENGTH6.getName());
						configRegexs.add(patternMap);
						break;
				}
			}
			returnMap.put("passwordStrategy",configRegexs);
		}
		Object licenseContent = request.getAttribute("licenseContent");//拦截器中获取到的证书数据
		if (licenseContent != null) {
			returnMap.put("licenseContent",licenseContent);
		}
		return Result.OK(returnMap);
	}
}

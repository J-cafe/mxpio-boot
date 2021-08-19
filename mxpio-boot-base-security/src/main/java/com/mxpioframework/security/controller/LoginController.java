package com.mxpioframework.security.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.service.UrlService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "LoginController", tags = { "系统接口" })
@RestController
@RequestMapping("/")
public class LoginController {

	@Autowired
	private UrlService urlService;

	@GetMapping("/loadUrl")
	@ApiOperation(value = "加载菜单")
	public Result<List<Url>> loadUrl() {
		return Result.OK(urlService.findTreeByUsername(null));
	}
}

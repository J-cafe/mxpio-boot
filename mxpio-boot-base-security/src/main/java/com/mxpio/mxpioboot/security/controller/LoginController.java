package com.mxpio.mxpioboot.security.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.security.vo.LoginUserVo;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "LoginController", tags = {"系统接口"})
@RestController
public class LoginController {

	/**
	 * 登录方法
	 * 
	 * @param user
	 *            登录信息
	 * @return 结果
	 */
	@PostMapping("/login")
	@ApiOperation(value = "登录")
	public Result<?> login(@RequestBody LoginUserVo user) {
		
		return Result.OK();
	}
}

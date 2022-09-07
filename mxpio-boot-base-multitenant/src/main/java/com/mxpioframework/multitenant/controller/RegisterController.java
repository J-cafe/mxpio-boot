package com.mxpioframework.multitenant.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.manager.service.RegisterService;
import com.mxpioframework.security.entity.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;


@Api(value = "RegisterController", tags = { "多租户注册接口" })
@RestController
@RequestMapping("/multitenant/register/")
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@PostMapping("organization")
	@ApiOperation(value = "企业注册", notes = "企业注册", httpMethod = "POST")
	public Result<User> registerOrganization(@RequestBody Map<String, Object> info) throws Exception {
		Organization organization = new Organization();
		organization.setId((String)info.get("organizationId"));
		organization.setName((String)info.get("organizationName"));
		User user = new User();
		user.setOrganization(organization);
		user.setAdministrator(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setCredentialsNonExpired(true);
		user.setUsername((String)info.get("username"));
		user.setNickname((String)info.get("nickname"));
		user.setPassword((String)info.get("password"));
		registerService.registerOrganization(user);
		return Result.OK(user);
	}
	
	@PostMapping("user")
	@ApiOperation(value = "用户注册", notes = "用户注册", httpMethod = "POST")
	public Result<User> registerUser(@RequestBody Map<String, Object> info) throws Exception {
		Organization organization = new Organization();
		organization.setId((String)info.get("organizationId"));
		organization.setName((String)info.get("organizationName"));
		User user = new User();
		user.setOrganization(organization);
		user.setUsername((String)info.get("username"));
		user.setNickname((String)info.get("nickname"));
		user.setPassword((String)info.get("password"));
		user.setAdministrator(false);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setCredentialsNonExpired(true);
		registerService.registerUser(user);
		return Result.OK(user);
	}
	
	@GetMapping("exist/user/{organizationId}/{username}")
	@ApiOperation(value = "判断用户是否存在", notes = "判断用户是否存在", httpMethod = "GET")
	public Result<Boolean> isExistUser(@PathVariable String organizationId, @PathVariable String username) throws Exception {
		return Result.OK(registerService.isExistUser(organizationId, username));
	}
	
	@GetMapping("exist/organization/{organizationId}")
	@ApiOperation(value = "判断企业是否存在", notes = "判断企业是否存在", httpMethod = "GET")
	public Result<Boolean> isExistOrganization(@PathVariable String organizationId) throws Exception {
		return  Result.OK(registerService.isExistOrganization(organizationId));
	}
	
	@GetMapping("organization/{organizationId}")
	@ApiOperation(value = "查询企业名称", notes = "查询企业名称", httpMethod = "GET")
	public Result<String> getOrganizationName(@PathVariable String organizationId) throws Exception {
		return Result.OK(registerService.getOrganization(organizationId).getName());
	}


}

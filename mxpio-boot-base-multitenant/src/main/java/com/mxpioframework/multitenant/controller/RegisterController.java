package com.mxpioframework.multitenant.controller;

import com.mxpioframework.multitenant.domain.dto.OrganizationUserDTO;
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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "RegisterController", description = "多租户注册接口")
@RestController("mxpio.multitenant.registerController")
@RequestMapping("/multitenant/register/")
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@PostMapping("organization")
	@Operation(summary = "企业注册", description = "企业注册", method = "POST")
	public Result<User> registerOrganization(@RequestBody OrganizationUserDTO organizationUser) {
		Organization organization = new Organization();
		organization.setId(organizationUser.getOrganizationId());
		organization.setName(organizationUser.getOrganizationName());
		organization.setDataSourceInfoId(organizationUser.getDataSourceInfoId());
		User user = new User();
		user.setOrganization(organization);
		user.setAdministrator(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setCredentialsNonExpired(true);
		user.setUsername(organizationUser.getUsername());
		user.setNickname(organizationUser.getNickname());
		user.setPassword(organizationUser.getPassword());
		registerService.registerOrganization(user);
		return Result.OK(user);
	}
	
	@PostMapping("user")
	@Operation(summary = "用户注册", description = "用户注册", method = "POST")
	public Result<User> registerUser(@RequestBody OrganizationUserDTO organizationUser) {
		Organization organization = new Organization();
		organization.setId(organizationUser.getOrganizationId());
		organization.setName(organizationUser.getOrganizationName());
		User user = new User();
		user.setOrganization(organization);
		user.setUsername(organizationUser.getUsername());
		user.setNickname(organizationUser.getNickname());
		user.setPassword(organizationUser.getPassword());
		user.setAdministrator(false);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		user.setCredentialsNonExpired(true);
		registerService.registerUser(user);
		return Result.OK(user);
	}
	
	@GetMapping("exist/user/{organizationId}/{username}")
	@Operation(summary = "判断用户是否存在", description = "判断用户是否存在", method = "GET")
	public Result<Boolean> isExistUser(@PathVariable String organizationId, @PathVariable String username) {
		return Result.OK(registerService.isExistUser(organizationId, username));
	}
	
	@GetMapping("exist/organization/{organizationId}")
	@Operation(summary = "判断企业是否存在", description = "判断企业是否存在", method = "GET")
	public Result<Boolean> isExistOrganization(@PathVariable String organizationId) {
		return  Result.OK(registerService.isExistOrganization(organizationId));
	}
	
	@GetMapping("organization/{organizationId}")
	@Operation(summary = "查询企业名称", description = "查询企业名称", method = "GET")
	public Result<String> getOrganizationName(@PathVariable String organizationId) {
		return Result.OK(registerService.getOrganization(organizationId).getName());
	}


}

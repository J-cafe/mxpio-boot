package com.mxpioframework.security.controller;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.service.UserService;
import com.mxpioframework.security.vo.UpatePassVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "UserController", description = "用户管理")
@RestController("mxpio.security.userController")
@RequestMapping("/user/")
public class UserController {
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("list")
	@Operation(summary = "用户列表", description = "根据过滤字段filter获取用户列表，过滤用户名和昵称", method = "GET")
	public Result<Page<User>> list(Criteria criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<User> page = userService.queryAll(criteria, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("info")
	@Operation(summary = "用户信息", description = "获取当前登录用户信息", method = "GET")
	public Result<UserDetails> info() throws Exception {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("user==>" + user);
		log.info("Authorities==>" + user.getAuthorities());
		return Result.OK(user);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加用户", description = "添加用户信息", method = "POST")
	public Result<UserDetails> add(@RequestBody User user) throws Exception {
		userService.create(user);
		return Result.OK("添加成功",user);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新用户", description = "更新用户信息", method = "PUT")
	public Result<User> edit(@RequestBody User user) throws Exception {
		userService.update(user);
		return Result.OK("编辑成功",null);
	}
	
	@PutMapping("updatepass")
	@Operation(summary = "修改密码", description = "修改密码", method = "PUT")
	public Result<User> updatePass(@RequestBody UpatePassVo upatePassVo) throws Exception {
		userService.updatePass(upatePassVo.getUsername(), upatePassVo.getNewPassword());
		return Result.OK("修改成功",null);
	}

	@PutMapping("updatepwdwithcheck")
	@Operation(summary = "修改密码", description = "修改密码", method = "PUT")
	public Result<User> updatepwdwithcheck(@RequestBody UpatePassVo upatePassVo) throws Exception {
		return userService.updatePassWithCheck(upatePassVo);
	}

	@DeleteMapping("delete")
	@Operation(summary = "删除用户", description = "根据用户名username删除用户信息", method = "DELETE")
	public Result<UserDetails> delete(String username) throws Exception {
		userService.delete(new HashSet<String>() {
			private static final long serialVersionUID = 1L;

			{
				add(username);
			}
		});
		return Result.OK("删除成功",null);
	}
	
	@PostMapping("logout")
	@Operation(summary = "强退用户", description = "根据用户名username强退用户", method = "POST")
	public Result<User> logout(@RequestParam("username") String username) throws Exception {
		onlineUserService.kickOutForUsername(username, cacheProvider);
		log.info("logout sucessful");
		return Result.OK();
	}
	
}

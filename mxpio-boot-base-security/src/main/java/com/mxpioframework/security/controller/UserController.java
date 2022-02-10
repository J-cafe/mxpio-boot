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
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.OnlineUserService;
import com.mxpioframework.security.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(value = "UserController", tags = {"用户管理"})
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private OnlineUserService onlineUserService;
	
	@Autowired
	private CacheProvider cacheProvider;
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
	@ApiOperation(value = "用户列表", notes = "根据过滤字段filter获取用户列表，过滤用户名和昵称", httpMethod = "GET")
	public Result<Page<User>> list(@RequestParam("criteria") String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<User> page = userService.queryAll(c, pageAble);
		return Result.OK(page);
	}
	
	@GetMapping("/info")
	@ApiOperation(value = "用户信息", notes = "获取当前登录用户信息", httpMethod = "GET")
	public Result<UserDetails> info() throws Exception {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("user==>" + user);
		log.info("Authorities==>" + user.getAuthorities());
		return Result.OK(user);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加用户", notes = "添加用户信息", httpMethod = "POST")
	public Result<UserDetails> add(@RequestBody User user) throws Exception {
		userService.create(user);
		return Result.OK("添加成功",user);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "更新用户", notes = "更新用户信息", httpMethod = "PUT")
	public Result<User> edit(@RequestBody User user) throws Exception {
		userService.update(user);
		return Result.OK("编辑成功",null);
	}
	
	@DeleteMapping("/delete")
	@ApiOperation(value = "删除用户", notes = "根据用户名username删除用户信息", httpMethod = "DELETE")
	public Result<UserDetails> delete(String username) throws Exception {
		userService.delete(new HashSet<String>() {
			private static final long serialVersionUID = 1L;

			{
				add(username);
			}
		});
		return Result.OK("删除成功",null);
	}
	
	@PostMapping("/logout")
	@ApiOperation(value = "强退用户", notes = "根据用户名username强退用户", httpMethod = "POST")
	public Result<User> logout(@RequestParam("username") String username) throws Exception {
		onlineUserService.kickOutForUsername(username, cacheProvider);
		log.info("logout sucessful");
		return Result.OK();
	}
	
}

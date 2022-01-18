package com.mxpioframework.security.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.cache.provider.CacheProvider;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.jpa.query.SimpleCriterion;
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
	@ApiOperation(value = "用户列表")
	public Result<Collection<User>> list(@PathParam("filter") String filter) throws Exception {
		List<User> list = new ArrayList<>();
		
		if(filter != null) {
			
			Criteria c = Criteria.create()
					.or()
						.addCriterion(new SimpleCriterion("username", Operator.LIKE, filter))
						.addCriterion(new SimpleCriterion("nickname", Operator.LIKE, filter))
					.end();
			list = userService.queryAll(c);
		}
		return Result.OK(list);
	}
	
	@GetMapping("/info")
	@ApiOperation(value = "用户信息")
	public Result<UserDetails> info() throws Exception {
		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("user==>" + user);
		log.info("Authorities==>" + user.getAuthorities());
		return Result.OK(user);
	}
	
	@PostMapping("/add")
	@ApiOperation(value = "添加用户")
	public Result<UserDetails> add(@RequestBody User user) throws Exception {
		userService.create(user);
		return Result.OK("添加成功",user);
	}
	
	@PutMapping("/edit")
	@ApiOperation(value = "编辑用户")
	public Result<User> edit(@RequestBody User user) throws Exception {
		userService.update(user);
		return Result.OK("编辑成功",null);
	}
	
	@DeleteMapping("/delete")
	@ApiOperation(value = "删除用户")
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
	@ApiOperation(value = "根据用户名强退用户")
	public Result<User> logout(@PathParam("username") String username) throws Exception {
		onlineUserService.kickOutForUsername(username, cacheProvider);
		log.info("logout sucessful");
		return Result.OK();
	}
	
}

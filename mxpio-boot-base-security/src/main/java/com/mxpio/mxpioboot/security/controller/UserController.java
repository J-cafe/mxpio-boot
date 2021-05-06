package com.mxpio.mxpioboot.security.controller;

import java.util.Collection;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mxpio.mxpioboot.common.vo.Result;
import com.mxpio.mxpioboot.jpa.query.Criteria;
import com.mxpio.mxpioboot.jpa.query.Junction;
import com.mxpio.mxpioboot.jpa.query.JunctionType;
import com.mxpio.mxpioboot.jpa.query.Operator;
import com.mxpio.mxpioboot.jpa.query.SimpleCriterion;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "UserController", tags = {"用户管理"})
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/list")
	@ApiOperation(value = "用户列表")
	public Result<Collection<User>> list(@PathParam("filter") String filter) throws Exception {
		Criteria criteria = new Criteria();
		if(filter != null) {
			Junction junction = new Junction(JunctionType.OR);
			junction.add(new SimpleCriterion("username", filter, Operator.LIKE));
			junction.add(new SimpleCriterion("nickname", filter, Operator.LIKE));
			criteria.add(junction);
		}
		return Result.OK(userService.queryAll(criteria));
	}
	
}

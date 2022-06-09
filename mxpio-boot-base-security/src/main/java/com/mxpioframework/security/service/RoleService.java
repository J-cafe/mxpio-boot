package com.mxpioframework.security.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.entity.User;

public interface RoleService {

	Page<Role> listPage(Criteria c, Pageable pageAble);

	List<Role> list(Criteria c);
	
	Role getById(String id);

	void save(Role role);

	void update(Role role);

	void delete(String key);
	
	Page<User> getUsersWithout(Pageable pageAble, Criteria criteria, String roleId);

	Page<User> getUsersWithin(Pageable pageAble, Criteria criteria, String roleId);

	void addActors(String id, List<String> usernames);

	void removeActors(String id, List<String> actorIds);

}
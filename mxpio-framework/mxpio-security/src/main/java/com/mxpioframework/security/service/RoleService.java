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
	
	Page<User> pagingUsersWithout(Pageable pageAble, Criteria criteria, String roleId);

	Page<User> pagingUsersWithin(Pageable pageAble, Criteria criteria, String roleId);

	List<User> getUsersWithin(Criteria criteria, String roleId);

	/**
	 * 根据用户ID查询已绑定的角色列表
	 * @param userId 用户ID（即用户名）
	 * @return 角色列表
	 */
	List<Role> getRolesByUserId(String userId);

	void addActors(String id, List<String> usernames);

	void removeActors(String id, List<String> actorIds);

}
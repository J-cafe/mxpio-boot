package com.mxpioframework.security.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	@Override
	@Transactional(readOnly = true)
	public Page<Role> listPage(Criteria c, Pageable pageAble) {
		return JpaUtil.linq(Role.class).where(c).paging(pageAble);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Role> list(Criteria c) {
		return JpaUtil.linq(Role.class).where(c).list();
	}

	@Override
	@Transactional(readOnly = true)
	public Role getById(String id) {
		return JpaUtil.linq(Role.class).idEqual(id).findOne();
	}

	@Override
	@Transactional(readOnly = false)
	@SecurityCacheEvict
	public void save(Role role) {
		JpaUtil.save(role);
	}

	@Override
	@Transactional(readOnly = false)
	@SecurityCacheEvict
	public void update(Role role) {
		JpaUtil.update(role);
	}

	@Override
	@Transactional(readOnly = false)
	@SecurityCacheEvict
	public void delete(String key) {
		Role role = getById(key);
		JpaUtil.remove(role);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> getUsersWithout(Pageable pageAble, Criteria criteria, String roleId) {
		return JpaUtil
				.linq(User.class)
				.where(criteria)
				.notExists(RoleGrantedAuthority.class)
					.equalProperty("actorId", "username")
					.equal("roleId", roleId)
				.end()
				.paging(pageAble);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> getUsersWithin(Pageable pageAble, Criteria criteria, String roleId) {
		return JpaUtil
				.linq(User.class)
				.where(criteria)
				.exists(RoleGrantedAuthority.class)
					.equalProperty("actorId", "username")
					.equal("roleId", roleId)
				.end()
				.paging(pageAble);
	}

	@Override
	@Transactional(readOnly = false)
	@SecurityCacheEvict
	public void addUsers(String id, List<String> actorIds) {
		for(String actorId : actorIds) {
			RoleGrantedAuthority authority = new RoleGrantedAuthority();
			authority.setActorId(actorId);
			authority.setRoleId(id);
			JpaUtil.save(authority);
		}
	}

	@Override
	@Transactional(readOnly = false)
	@SecurityCacheEvict
	public void removeUsers(String id, List<String> actorIds) {
		JpaUtil.lind(RoleGrantedAuthority.class).equal("roleId", id).in("actorId", actorIds).delete();
	}

}

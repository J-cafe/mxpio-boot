package com.mxpioframework.security.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.initiator.JpaUtilAble;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Operator;
import com.mxpioframework.jpa.query.SimpleCriterion;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.UserService;

@Service
public class UserServiceImpl implements UserService, JpaUtilAble {
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly = false)
	public User create(User user) {
		user.setPassword(passwordEncoder.encode("123456"));
		JpaUtil.save(user);
		return user;
	}

	@Override
	@Transactional(readOnly = false)
	public void update(User user) throws Exception {
		User temp = JpaUtil.getOne(User.class, user.getUsername());
		user.setPassword(temp.getPassword());
		JpaUtil.update(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Set<String> usernames) {
		JpaUtil.lind(User.class).in("username", usernames).delete();
	}

	@Override
	@Transactional(readOnly = true)
	public User findByName(String username) {
		return JpaUtil.getOne(User.class, username);
	}

	@Override
	@Transactional(readOnly = false)
	public void updatePass(String username, String newPassword) {
		User u = JpaUtil.getOne(User.class, username);
		u.setPassword(passwordEncoder.encode(newPassword));
	}

	@Override
	@Transactional(readOnly = false)
	public Map<String, String> updateAvatar(MultipartFile file) {
		return null;
	}

	@Override
	@Transactional(readOnly = false)
	public void updateEmail(String username, String email) {
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<User> queryAll(Criteria criteria, Pageable pageable) {
		return JpaUtil.linq(User.class).where(criteria).paging(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> queryAll(Criteria criteria) {
		return JpaUtil.linq(User.class).where(criteria).list();
	}
	
	@Override
	@Transactional(readOnly = true)
	public void download(List<User> queryAll, HttpServletResponse response) throws IOException {

	}

	@Override
	@Transactional(readOnly = false)
	public void updateCenter(User resources) {

	}

	@Override
	@Transactional(readOnly = true)
	public boolean isAdministrator() {
		return isAdministrator(null);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isAdministrator(String username) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof User) {
			if (((User) principal).getUsername().equals(username)) {
				return ((User) principal).isAdministrator();
			} 
			if (username == null) {
				return ((User) principal).isAdministrator();
			}
		}
		User user = JpaUtil.linq(User.class).idEqual(username).findOne();
		return user.isAdministrator();
	}

	@Override
	@Transactional(readOnly = false)
	public void afterPropertiesSet(ApplicationContext applicationContext) {
		
		SimpleCriterion sc = new SimpleCriterion("resourceType", Operator.EQ, Element.RESOURCE_TYPE);
		SimpleCriterion sc1 = new SimpleCriterion("roleId", Operator.EQ, "111");
		SimpleCriterion sc2 = new SimpleCriterion("id", Operator.EQ, "p111");
		
		Criteria c = Criteria.create().addCriterion(sc).or().addCriterion(sc1).addCriterion(sc2);
	
		// String json = "{\"criterions\":[{\"fieldName\":\"resourceType\",\"operator\":\"EQ\",\"value\":\"ELEMENT\"},{\"criterions\":[{\"fieldName\":\"roleId\",\"operator\":\"EQ\",\"value\":\"111\"},{\"fieldName\":\"id\",\"operator\":\"EQ\",\"value\":\"p111\"}],\"type\":\"OR\"}],\"orders\":[]}";
		
		// Criteria c2 = CriteriaUtils.json2Criteria(json);
		Pageable page = PageRequest.of(0, 10);
		
		JpaUtil.linq(Permission.class)
			.where(c)
			.collect(Role.class, "roleId")
			.collect(Element.class, "resourceId")
			.exists(RoleGrantedAuthority.class)
				.equalProperty("roleId", "roleId")
				.equal("actorId", "admin")
			.end()
			.paging(page);		
	}

}

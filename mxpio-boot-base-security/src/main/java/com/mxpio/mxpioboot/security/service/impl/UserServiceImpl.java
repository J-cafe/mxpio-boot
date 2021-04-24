package com.mxpio.mxpioboot.security.service.impl;

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

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.jpa.initiator.JpaUtilAble;
import com.mxpio.mxpioboot.jpa.query.Criteria;
import com.mxpio.mxpioboot.jpa.query.CriteriaUtils;
import com.mxpio.mxpioboot.jpa.query.Junction;
import com.mxpio.mxpioboot.jpa.query.JunctionType;
import com.mxpio.mxpioboot.jpa.query.Operator;
import com.mxpio.mxpioboot.jpa.query.SimpleCriterion;
import com.mxpio.mxpioboot.security.entity.Element;
import com.mxpio.mxpioboot.security.entity.Permission;
import com.mxpio.mxpioboot.security.entity.Role;
import com.mxpio.mxpioboot.security.entity.RoleGrantedAuthority;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.UserService;

@Service
public class UserServiceImpl implements UserService, JpaUtilAble {
	
	@Autowired
	protected PasswordEncoder passwordEncoder;
	
	@Override
	@Transactional(readOnly = false)
	public void create(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		JpaUtil.save(user);
	}

	@Override
	@Transactional(readOnly = false)
	public void update(User user) throws Exception {
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
	@Transactional(readOnly = true)
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
		
		SimpleCriterion sc = new SimpleCriterion("resourceType", Element.RESOURCE_TYPE, Operator.EQ);
		SimpleCriterion sc1 = new SimpleCriterion("roleId", "111", Operator.EQ);
		SimpleCriterion sc2 = new SimpleCriterion("id", "p111", Operator.EQ);
		
		Criteria c = new Criteria();
		c.add(sc);
		Junction or = new Junction(JunctionType.OR);
		or.add(sc1);
		or.add(sc2);
		c.add(or);
		
		
		
		String json = "{\"criterions\":[{\"fieldName\":\"resourceType\",\"operator\":\"EQ\",\"value\":\"ELEMENT\"},{\"criterions\":[{\"fieldName\":\"roleId\",\"operator\":\"EQ\",\"value\":\"111\"},{\"fieldName\":\"id\",\"operator\":\"EQ\",\"value\":\"p111\"}],\"type\":\"OR\"}],\"orders\":[]}";
		
		Criteria c2 = CriteriaUtils.json2Criteria(json);
		Pageable page = PageRequest.of(0, 10);
		
		JpaUtil.linq(Permission.class)
			.where(c2)
			.collect(Role.class, "roleId")
			.collect(Element.class, "resourceId")
			.exists(RoleGrantedAuthority.class)
				.equalProperty("roleId", "roleId")
				.equal("actorId", "admin")
			.end()
			.paging(page);		
	}

}

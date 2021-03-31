package com.mxpio.mxpioboot.security.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaQuery;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mxpio.mxpioboot.jpa.JpaUtil;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
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
	public Map<String, String> updateAvatar(MultipartFile file) {
		return null;
	}

	@Override
	public void updateEmail(String username, String email) {
		
	}

	@Override
	public Page<User> queryAll(CriteriaQuery<User> criteria, Pageable pageable) {
		return null;
	}

	@Override
	public List<User> queryAll(CriteriaQuery<User> criteria) {
		return null;
	}

	@Override
	public void download(List<User> queryAll, HttpServletResponse response) throws IOException {

	}

	@Override
	public void updateCenter(User resources) {

	}

	@Override
	public boolean isAdministrator() {
		return isAdministrator(null);
	}

	@Override
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

}

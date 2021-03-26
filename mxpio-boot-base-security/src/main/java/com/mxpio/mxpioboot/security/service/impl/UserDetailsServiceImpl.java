package com.mxpio.mxpioboot.security.service.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mxpio.mxpioboot.common.exception.MBootException;
import com.mxpio.mxpioboot.security.entity.User;
import com.mxpio.mxpioboot.security.service.UserService;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserService userService;

	@Override
	public User loadUserByUsername(String username) throws UsernameNotFoundException {
		User user;
        try {
            user = userService.findByName(username);
        } catch (EntityNotFoundException e) {
            // SpringSecurity会自动转换UsernameNotFoundException为BadCredentialsException
            throw new UsernameNotFoundException("", e);
        }
        if (user == null) {
            throw new UsernameNotFoundException("");
        } else {
            if (!user.isEnabled()) {
                throw new MBootException("账号未激活！");
            }
            //TODO 添加权限、部门信息
        }
		return user;
	}
}

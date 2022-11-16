package com.mxpioframework.security.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.GrantedAuthorityService;

import java.util.Date;

/**
 * Spring Security的{@link org.springframework.security.core.userdetails.UserDetailsService}接口的默认实现
 */
@Service
@Transactional(readOnly = true)
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private GrantedAuthorityService grantedAuthorityService;

	@Value("${mxpio.password.expireddays}")
	private String expireddays;
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			
			User user = JpaUtil.getOne(User.class, username);
			user.setAuthorities(grantedAuthorityService.getGrantedAuthorities(user));
			Date pwdEffectDate = user.getPwdUpdateTime()==null?user.getCreateTime(): user.getPwdUpdateTime();
			if (pwdEffectDate==null){
				throw new UsernameNotFoundException("获取用户创建时间失败，请检查数据");
			}
			int days = (int) ((new Date().getTime() - pwdEffectDate.getTime()) / (1000*3600*24));
			if (days>=Integer.parseInt(expireddays)){
				user.setPwdExpiredFlag(true);
			}
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("Not Found");
		}
	}
}

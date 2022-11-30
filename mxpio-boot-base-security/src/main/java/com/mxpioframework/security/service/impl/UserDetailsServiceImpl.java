package com.mxpioframework.security.service.impl;

import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.UserDept;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

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
	@Value("${mxpio.password.expiredswitch}")
	private String expiredswitch;
	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			
			User user = JpaUtil.getOne(User.class, username);
			user.setAuthorities(grantedAuthorityService.getGrantedAuthorities(user));
			if(StringUtils.equals("on",expiredswitch)){
				Date pwdEffectDate = user.getPwdUpdateTime();
				if (pwdEffectDate==null){
					//throw new UsernameNotFoundException("获取用户创建时间失败，请检查数据");
					user.setPwdExpiredFlag(true);
				}else{
					int days = (int) ((new Date().getTime() - pwdEffectDate.getTime()) / (1000*3600*24));
					if (days>=Integer.parseInt(expireddays)){
						user.setPwdExpiredFlag(true);
					}
				}
			}
			List<Dept> depts = JpaUtil.linq(Dept.class)
					.exists(UserDept.class)
					.equalProperty("deptId", "id")
					.equal("userId", user.getUsername())
					.end()
					.list();
			if (depts.size()>0){
				user.setOrganization(depts.get(0));
			}
			return user;
		} catch (Exception e) {
			throw new UsernameNotFoundException("用户名未找到");
		}
	}
}

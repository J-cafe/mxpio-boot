package com.mxpioframework.autoconfigure.multitenant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.multitenant.MultitenantUtils;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.service.OrganizationService;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.GrantedAuthorityService;
import com.mxpioframework.security.util.SecurityUtils;

/**
 * Spring Security的{@link org.springframework.security.core.userdetails.UserDetailsService}接口的默认实现
 * @author Kevin Yang (mailto:kevin.yang@bstek.com)
 * @since 2016年2月27日
 */
@Transactional(readOnly = true)
public class MultitenantUserDetailsService implements UserDetailsService {
	
	@Autowired  
	private HttpServletRequest request;
	
	@Autowired
	private GrantedAuthorityService grantedAuthorityService;
	
	@Autowired
	private OrganizationService organizationService;
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {			
		try {
			Organization organization = loadOrganization();
			return MultitenantUtils.doQuery(organization.getId(), () ->  {
				User user = JpaUtil.getOne(User.class, username);
				user.setOrganization(organization);
				user.setAuthorities(grantedAuthorityService.getGrantedAuthorities(user));
				return user;
			});
		} catch (Exception e) {
			throw new UsernameNotFoundException(e.getMessage());
		}

	}

	private Organization loadOrganization() {
		User user = SecurityUtils.getLoginUser();
		Organization organization = null;
		if (user == null) {
			String organizationId = request.getParameter("organization");
			organization = organizationService.get(organizationId);
			Assert.notNull(organization, "Organization is not exists.");
		} else {
			organization = user.getOrganization();
		}
		return organization;
	}

}

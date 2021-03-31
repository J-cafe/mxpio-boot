package com.mxpio.mxpioboot.security.access.vote;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.mxpio.mxpioboot.security.entity.Url;

/**

 * 菜单角色投票器

 * @author Kevin Yang (mailto:kevin.yang@bstek.com)

 * @since 2016年7月7日

 */
@Component
public class UrlRoleVoter implements AccessDecisionVoter<Object> {

	private String rolePrefix = "ROLE_";
	
	public String getRolePrefix() {
		return rolePrefix;
	}

	public void setRolePrefix(String rolePrefix) {
		this.rolePrefix = rolePrefix;
	}

	public boolean supports(ConfigAttribute attribute) {
		if ((attribute.getAttribute() != null)
				&& attribute.getAttribute().startsWith(getRolePrefix())) {
			return true;
		}else {
			return false;
		}
	}

	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz) || Url.class.isAssignableFrom(clazz);
	}

	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		if(authentication == null) {
			return ACCESS_DENIED;
		}
		
		int result = ACCESS_ABSTAIN;
		Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;

				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().equals(authority.getAuthority())) {
						return ACCESS_GRANTED;
					}
				}
			}
		}

		return result;
	}

	Collection<? extends GrantedAuthority> extractAuthorities(
			Authentication authentication) {
		if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			return userDetails.getAuthorities();
		}
		return authentication.getAuthorities();
	}
}

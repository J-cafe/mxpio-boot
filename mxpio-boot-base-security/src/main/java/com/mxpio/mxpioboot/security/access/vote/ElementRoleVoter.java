package com.mxpio.mxpioboot.security.access.vote;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mxpio.mxpioboot.security.entity.Element;

/**
 * 组件角色投票器
 */
@Component
public class ElementRoleVoter implements AccessDecisionVoter<Object> {

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
		}
		else {
			return false;
		}
	}

	public boolean supports(Class<?> clazz) {
		return Component.class.isAssignableFrom(clazz);
	}

	public int vote(Authentication authentication, Object object,
			Collection<ConfigAttribute> attributes) {
		if(authentication == null) {
			return ACCESS_DENIED;
		}
		if (CollectionUtils.isEmpty(attributes)) {
			return ACCESS_GRANTED;
		}
		int result = ACCESS_ABSTAIN;
		Element element = (Element) object;
		String elementType = "";
		if (element.getElementType() != null) {
			elementType = element.getElementType().name();
		}
		Collection<? extends GrantedAuthority> authorities = extractAuthorities(authentication);

		for (ConfigAttribute attribute : attributes) {
			if (this.supports(attribute)) {
				result = ACCESS_DENIED;

				for (GrantedAuthority authority : authorities) {
					if (attribute.getAttribute().startsWith((authority.getAuthority() + "_" + elementType))) {
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

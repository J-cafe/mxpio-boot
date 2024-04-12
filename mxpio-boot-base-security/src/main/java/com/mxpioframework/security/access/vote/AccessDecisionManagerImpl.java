package com.mxpioframework.security.access.vote;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.mxpioframework.security.service.UserService;

/**
 * 默认访问决策管理器
 */
@Component
public class AccessDecisionManagerImpl extends AbstractAccessDecisionManager {

	@Value("${mxpio.allowIfAllAbstainDecisions:true}")
	private boolean allowIfAllAbstainDecisions;
	
	@Autowired
	private UserService userService;
	
	private List<AccessDecisionVoter<? extends Object>> decisionVoters;

	
	@Autowired
	public AccessDecisionManagerImpl(
			List<AccessDecisionVoter<? extends Object>> decisionVoters) {
		super(decisionVoters);
		this.decisionVoters = decisionVoters;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void decide(Authentication authentication, Object object,
			Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		if (userService.isAdministrator()) {
			return;
		}
		int deny = 0;
		for (AccessDecisionVoter voter : getDecisionVoters()) {
			if (voter.supports(object.getClass())) {
				int result = voter.vote(authentication, object, configAttributes);
				if (logger.isDebugEnabled()) {
					logger.debug("Voter: " + voter + ", returned: " + result);
				}
				switch (result) {
				case AccessDecisionVoter.ACCESS_GRANTED:
					return;
				case AccessDecisionVoter.ACCESS_DENIED:
					deny++;
					break;
				default:
					break;
				}
			}
		}

		if (deny > 0) {
			throw new AccessDeniedException(messages.getMessage(
					"AbstractAccessDecisionManager.accessDenied", "Access is denied"));
		}

		setAllowIfAllAbstainDecisions(allowIfAllAbstainDecisions);
		checkAllowIfAllAbstainDecisions();

	}
	
	public boolean supports(Class<?> clazz) {
		for (AccessDecisionVoter<?> voter : this.decisionVoters) {
			if (voter.supports(clazz)) {
				return true;
			}
		}

		return false;
	}

}
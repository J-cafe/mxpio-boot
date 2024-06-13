package com.mxpioframework.security.access.intercept;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

public class FilterSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {
	private FilterInvocationSecurityMetadataSource securityMetadataSource;
	private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
	private boolean observeOncePerRequest = true;
	public void init(FilterConfig arg0) {
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		this.invoke(new FilterInvocation(request, response, chain));
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource newSource) {
		this.securityMetadataSource = newSource;
	}

	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	public void invoke(FilterInvocation filterInvocation) throws IOException, ServletException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (this.isApplied(filterInvocation) && this.observeOncePerRequest
				|| (auth.isAuthenticated() && auth.getPrincipal() instanceof String && "anonymousUser".equals(auth.getPrincipal()))) {
			filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
		} else {
			if (filterInvocation.getRequest() != null && this.observeOncePerRequest) {
				filterInvocation.getRequest().setAttribute(FILTER_APPLIED,
						Boolean.TRUE);
			}

			InterceptorStatusToken token = super.beforeInvocation(filterInvocation);

			try {
				filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
			} finally {
				super.finallyInvocation(token);
			}

			super.afterInvocation(token, (Object) null);
		}
	}

	private boolean isApplied(FilterInvocation filterInvocation) {
		return filterInvocation.getRequest() != null && filterInvocation.getRequest()
				.getAttribute("__spring_security_filterSecurityInterceptor_filterApplied") != null;
	}

	public boolean isObserveOncePerRequest() {
		return this.observeOncePerRequest;
	}

	public void setObserveOncePerRequest(boolean observeOncePerRequest) {
		this.observeOncePerRequest = observeOncePerRequest;
	}

}

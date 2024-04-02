package com.mxpioframework.security.access.provider.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.access.provider.GrantedAuthorityProvider;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Component
@Order(3)
public class PostGrantedAuthorityProvider implements GrantedAuthorityProvider  {
	
	@Autowired
	private PostService postService;
	
	@Override
	public Collection<? extends GrantedAuthority> provide(UserDetails userDetails) {
		Set<String> postIds = postService.getPostKeyByUser(userDetails.getUsername());
		List<GrantedAuthority> authorities = null;
		
		for(String postId : postIds){
			if(authorities == null){
				authorities = new ArrayList<GrantedAuthority>();
			}
			List<RoleGrantedAuthority> actorId = JpaUtil.linq(RoleGrantedAuthority.class)
					.equal("actorId", postId)
					.list();
			authorities.addAll(actorId);
		}
		
		return authorities;
	}
}

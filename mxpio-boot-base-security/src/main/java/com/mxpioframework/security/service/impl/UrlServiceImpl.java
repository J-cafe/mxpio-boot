package com.mxpioframework.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.decision.manager.SecurityDecisionManager;
import com.mxpioframework.security.entity.DataResource;
import com.mxpioframework.security.entity.Element;
import com.mxpioframework.security.entity.Permission;
import com.mxpioframework.security.entity.ResourceType;
import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.GrantedAuthorityService;
import com.mxpioframework.security.service.UrlService;
import com.mxpioframework.security.service.UrlServiceCache;
import com.mxpioframework.security.service.UserService;

@Service
@Transactional(readOnly = true)
public class UrlServiceImpl extends BaseServiceImpl<Url> implements UrlService {
	@Autowired
	private UrlServiceCache urlServiceCache;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityDecisionManager securityDecisionManager;
	
	@Autowired
	private GrantedAuthorityService grantedAuthorityService;
	
	@Override
	public List<Url> findAll() {
		List<Url> urls = JpaUtil.linq(Url.class).list();
		List<Permission> permissions = JpaUtil.linq(Permission.class).equal("resourceType", ResourceType.URL).list();
		if (!permissions.isEmpty()) {
			Map<String, Url> urlMap = JpaUtil.index(urls);
			for (Permission permission : permissions) {
				Url url = urlMap.get(permission.getResourceId());
				List<ConfigAttribute> configAttributes = url.getAttributes();
				if (configAttributes == null) {
					configAttributes = new ArrayList<ConfigAttribute>();
					url.setAttributes(configAttributes);
				}
				configAttributes.add(permission);
			}
		}
		return urls;
	}
	
	public List<Url> findTree() {
		return urlServiceCache.findTree();
	}
	
	public List<Url> findAllTree() {
		List<Url> result = new ArrayList<Url>();
		List<Url> urls = JpaUtil.linq(Url.class).asc("order").list();
		Map<String, Url> urlMap = new HashMap<String, Url>(urls.size());
		Map<String, List<Url>> childrenMap = new HashMap<String, List<Url>>(urls.size());
		
		List<DataResource> datas = JpaUtil.linq(DataResource.class).asc("order").list();
		Map<String, List<DataResource>> dataMap = JpaUtil.classify(datas, "parentId");

		for (Url url : urls) {
			urlMap.put(url.getId(), url);
			
			if(dataMap.containsKey(url.getId())){
				url.setDatas(dataMap.get(url.getId()));
			}
			
			if (childrenMap.containsKey(url.getId())) {
				url.setChildren(childrenMap.get(url.getId()));
			} else {
				url.setChildren(new ArrayList<Url>());
				childrenMap.put(url.getId(), url.getChildren());
			}

			if (!StringUtils.hasText(url.getParentId())) {
				result.add(url);
			} else {
				List<Url> children;
				if (childrenMap.containsKey(url.getParentId())) {
					children = childrenMap.get(url.getParentId());
				} else {
					children = new ArrayList<Url>();
					childrenMap.put(url.getParentId(), children);
				}
				children.add(url);
			}
		}

		return result;
	}
	
	@Override
	public List<Url> findTreeByUsername(String username) {
		List<Url> urls = urlServiceCache.findTree();
		List<Url> result = new ArrayList<Url>();
		rebuildLoginUserGrantedAuthorities();
		for (Url url : urls) {
			Url copy = copyFor(url);
			if (decide(username, copy, url, userService.isAdministrator())) {
				result.add(copy);
			}
		}
		return result;
	}
	
	protected Url copyFor(Url url) {
		Url u = new Url();
		u.setId(url.getId());
		u.setIcon(url.getIcon());
		u.setDescription(url.getDescription());
		u.setName(url.getName());
		u.setNavigable(url.isNavigable());
		u.setOrder(url.getOrder());
		u.setParentId(url.getParentId());
		u.setPath(url.getPath());
		u.setComponent(url.getComponent());
		u.setKeepAlive(url.isKeepAlive());
		u.setAttributes(url.getAttributes());
		u.setOutside(url.isOutside());
		u.setTitle(url.getTitle());
		return u;
	}
	
	@Override
	public List<Url> getAccessibleUrlsByUsername(String username) {
		List<Url> urls = urlServiceCache.findTree();
		List<Url> result = new ArrayList<Url>();
		for (Url url : urls) {
			Url copy = copyFor(url);
			if (decide(username, copy, url, userService.isAdministrator(username))) {
				result.add(copy);
			}
		}
		return result;
	}
		
	private void rebuildLoginUserGrantedAuthorities() {
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		user.setAuthorities(grantedAuthorityService.getGrantedAuthorities(user));
	}

	private boolean decide(String username, Url copy, Url url, boolean administrator) {
		/*
		 * if (!url.isNavigable()) { return false; }
		 */
		if (administrator || securityDecisionManager.decide(username, url)) {
			List<Url> children = url.getChildren();
			List<Url> newChildren = new ArrayList<Url>();
			copy.setChildren(newChildren);
			if (!CollectionUtils.isEmpty(children)) {
				for (Url child : children) {
					Url copyChild = copyFor(child);
					if (decide(username, copyChild, child, administrator)) {
						newChildren.add(copyChild);
					}
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void save(List<Url> url) {
		JpaUtil.save(url);
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void update(List<Url> url) {
		JpaUtil.update(url);
	}
	
	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public void delete(List<Url> url) {
		JpaUtil.delete(url);
	}

	@Override
	@SecurityCacheEvict
	@Transactional(readOnly = false)
	public boolean deleteBundleById(String id) {
		Long elementCount = JpaUtil.linq(Element.class).equal("parentId",id).count();
		if(elementCount > 0) {
			return false;
		}else {
			JpaUtil.lind(Permission.class).equal("resourceId", id).delete();
			JpaUtil.lind(DataResource.class).equal("parentId", id).delete();
			delete(id, Url.class);
			return true;
		}
		
	}

}

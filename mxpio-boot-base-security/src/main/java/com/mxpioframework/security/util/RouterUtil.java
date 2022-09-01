package com.mxpioframework.security.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.mxpioframework.security.entity.Url;
import com.mxpioframework.security.vo.RouterMetaVo;
import com.mxpioframework.security.vo.RouterVo;

public class RouterUtil {
	public static List<RouterVo> buildRouter(List<Url> urls) {
		List<RouterVo> routers = new ArrayList<>();
		for (Url url : urls) {
			RouterMetaVo meta = new RouterMetaVo();
			meta.setHidden(url.isNavigable());
			meta.setIcon(url.getIcon());
			meta.setKeepAlive(url.isKeepAlive());
			meta.setOutside(url.isOutside());
			meta.setTitle(url.getTitle());
			meta.setOrder(url.getOrder());
			meta.setDesc(url.getDescription());
			RouterVo router = RouterVo.builder().key(url.getId()).parentId(url.getParentId()).name(url.getName())
					.meta(meta).component(url.getComponent()).path(url.getPath()).build();
			if (CollectionUtils.isNotEmpty(url.getChildren())) {
				router.setChildren(buildRouter(url.getChildren()));
			}
			routers.add(router);
		}
		return routers;
	}

	public static Url router2Url(RouterVo router) {
		Url url = new Url();
		url.setId(router.getKey());
		url.setComponent(router.getComponent());
		url.setDescription(router.getMeta().getDesc());
		url.setIcon(router.getMeta().getIcon());
		url.setName(router.getName());
		url.setTitle(router.getMeta().getTitle());
		url.setOrder(router.getMeta().getOrder());
		url.setNavigable(router.getMeta().isHidden());
		url.setPath(router.getPath());
		url.setKeepAlive(router.getMeta().isKeepAlive());
		url.setParentId(router.getParentId());

		return url;
	}
}

package com.mxpioframework.security.service;

import com.mxpioframework.security.entity.PostType;

public interface PostTypeService extends BaseService<PostType> {


	void deletePostTypes(String[] postTypeId);
}

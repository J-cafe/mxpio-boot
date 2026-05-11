package com.mxpioframework.security.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.security.entity.PostType;
import com.mxpioframework.security.service.PostTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("mxpio.security.postTypeService")
public class PostTypeServiceImpl extends BaseServiceImpl<PostType> implements PostTypeService {
    @Override
    @Transactional
    public void deletePostTypes(String[] postTypeId) {
        for (String id : postTypeId){
            PostType postType = JpaUtil.getOne(PostType.class, id);
            JpaUtil.delete(postType);
        }
    }
}

package com.mxpioframework.security.service.impl;

import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.Order;
import com.mxpioframework.security.cache.SecurityCacheEvict;
import com.mxpioframework.security.common.Constants;
import com.mxpioframework.security.entity.Post;
import com.mxpioframework.security.entity.RoleGrantedAuthority;
import com.mxpioframework.security.entity.User;
import com.mxpioframework.security.service.PostService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("mxpio.security.postService")
public class PostServiceImpl extends BaseServiceImpl<Post> implements PostService {
    @Override
    @Transactional
    @SecurityCacheEvict
    public void savePosts(List<Post> posts) {
        JpaUtil.save(posts);
    }

    @Override
    @Transactional
    @SecurityCacheEvict
    public void savePost(Post post) {
        JpaUtil.save(post);
    }

    @Override
    @Transactional
    @SecurityCacheEvict
    public void updatePosts(List<Post> posts) {
        JpaUtil.update(posts);
    }

    @Override
    @Transactional
    @SecurityCacheEvict
    public void updatePost(Post post) {
        JpaUtil.update(post);
    }

    @Override
    @Transactional
    @SecurityCacheEvict
    public void deletePosts(String[] postId) {
        for (String id : postId){
            Post post =JpaUtil.getOne(Post.class, id);
            JpaUtil.delete(post);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> loadPostsWithout(Pageable pageable, Criteria criteria, String roleId) {
        List<Order> orders = new ArrayList<Order>();
        if(criteria != null){
            orders = criteria.getOrders();
        }
        return JpaUtil
                .linq(Post.class)
                .where(criteria)
                .notExists(RoleGrantedAuthority.class)
                    .equalProperty("actorId", "id")
                    .equal("roleId", roleId)
                .end()
                .addIfNot(orders)
                .asc("id")
                .endIf()
                .paging(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Post> loadPostsWithin(Pageable pageable, Criteria criteria, String roleId) {
        List<Order> orders = new ArrayList<Order>();
        if(criteria != null){
            orders = criteria.getOrders();
        }
        return JpaUtil
                .linq(Post.class)
                .where(criteria)
                .exists(RoleGrantedAuthority.class)
                    .equalProperty("actorId", "id")
                    .equal("roleId", roleId)
                .end()
                .addIfNot(orders)
                .asc("id")
                .endIf()
                .paging(pageable);
    }

}

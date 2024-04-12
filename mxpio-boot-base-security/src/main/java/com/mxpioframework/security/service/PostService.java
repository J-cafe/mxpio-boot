package com.mxpioframework.security.service;

import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface PostService extends BaseService<Post> {

	/**
	 * 新增职位信息
	 * @param posts 职位信息
	 */
	void savePosts(List<Post> posts);
	
	/**
	 * 新增职位信息
	 * @param post 职位信息
	 */
	void savePost(Post post);
	
	/**
	 * 更新职位信息
	 * @param posts 职位信息
	 */
	void updatePosts(List<Post> posts);
	
	/**
	 * 更新职位信息
	 * @param post 职位信息
	 */
	void updatePost(Post post);
	
	/**
	 * 删除职位信息
	 * @param postId 职位ID
	 */
	void deletePosts(String[] postId);
	
	/**
	 * 分页获取未绑定角色ID的职位
	 * @param pageable 分页对象
	 * @param criteria 查询过滤器
	 * @param roleId 角色ID
	 * @return 职位分页数据
	 */
	Page<Post> loadPostsWithout(Pageable pageable, Criteria criteria, String roleId);
	
	/**
	 * 分页获取绑定角色ID的职位
	 * @param pageable 分页对象
	 * @param criteria 查询过滤器
	 * @param roleId 角色ID
	 * @return 职位分页数据
	 */
	Page<Post> loadPostsWithin(Pageable pageable, Criteria criteria, String roleId);

}

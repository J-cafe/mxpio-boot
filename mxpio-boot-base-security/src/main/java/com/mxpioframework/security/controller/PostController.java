package com.mxpioframework.security.controller;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Dept;
import com.mxpioframework.security.entity.Post;
import com.mxpioframework.security.entity.Role;
import com.mxpioframework.security.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PostController", description = "岗位管理")
@RestController("mxpio.security.postController")
@RequestMapping("/post/")
public class PostController {
	
	@Autowired
	private PostService postService;

	@GetMapping("page")
	@Operation(summary = "岗位列表(分页)", description = "获取岗位列表(分页)", method = "GET")
	public Result<Page<Post>> page(Criteria criteria,
								   @RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
								   @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<Post> page = postService.listPage(Post.class, pageAble, criteria);
		return Result.OK(page);
	}

	@GetMapping("list")
	@Operation(summary = "岗位列表", description = "获取岗位列表", method = "GET")
	public Result<List<Post>> page(Criteria criteria) throws Exception {
		List<Post> posts = postService.list(Post.class, criteria);
		return Result.OK(posts);
	}

	@GetMapping("role/without/{roleId}")
	@Operation(summary = "未绑定岗位", description = "分页获取未绑定角色ID的岗位", method = "GET")
	public Result<Page<Post>> without(Criteria criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(postService.loadPostsWithout(pageAble, criteria, roleId));
	}
	
	@GetMapping("role/within/{roleId}")
	@Operation(summary = "绑定岗位", description = "分页获取绑定角色ID的岗位", method = "GET")
	public Result<Page<Post>> within(Criteria criteria,
			@PathVariable(value = "roleId") String roleId,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		return Result.OK(postService.loadPostsWithin(pageAble, criteria, roleId));
	}
	
	@PostMapping("add")
	@Operation(summary = "添加岗位", description = "添加岗位信息", method = "POST")
	public Result<List<Post>> add(@RequestBody List<Post> posts) throws Exception {
		postService.savePosts(posts);
		return Result.OK("添加岗位成功",posts);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新岗位", description = "更新岗位信息", method = "PUT")
	public Result<List<Post>> edit(@RequestBody List<Post> posts) throws Exception {
		postService.updatePosts(posts);
		return Result.OK("更新岗位成功",posts);
	}
	
	@DeleteMapping("remove/{postIds}")
	@Operation(summary = "删除岗位", description = "删除岗位信息", method = "DELETE")
	public Result<List<Post>> remove(@PathVariable(name = "postIds") String postIds) throws Exception {
		String[] postId = postIds.split(",");
		postService.deletePosts(postId);
		return Result.OK("删除岗位成功",null);
	}

}

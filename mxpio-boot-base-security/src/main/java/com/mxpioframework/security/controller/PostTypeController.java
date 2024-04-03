package com.mxpioframework.security.controller;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.Post;
import com.mxpioframework.security.entity.PostType;
import com.mxpioframework.security.service.PostTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "PostController", description = "职系管理")
@RestController("mxpio.security.postTypeController")
@RequestMapping("/posttype/")
public class PostTypeController {
	
	@Autowired
	private PostTypeService postTypeService;

	@GetMapping("page")
	@Operation(summary = "职系列表(分页)", description = "获取职系列表(分页)", method = "GET")
	public Result<Page<PostType>> page(Criteria criteria,
									   @RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
									   @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<PostType> page = postTypeService.listPage(PostType.class, pageAble, criteria);
		return Result.OK(page);
	}

	@GetMapping("list")
	@Operation(summary = "职系列表", description = "获取职系列表", method = "GET")
	public Result<List<PostType>> page(Criteria criteria) throws Exception {
		List<PostType> postTypes = postTypeService.list(PostType.class, criteria);
		return Result.OK(postTypes);
	}

	@PostMapping("add")
	@Operation(summary = "添加职系", description = "添加职系信息", method = "POST")
	public Result<List<PostType>> add(@RequestBody List<PostType> postTypes) throws Exception {
		postTypeService.save(postTypes);
		return Result.OK("添加职系成功",postTypes);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新职系", description = "更新职系信息", method = "PUT")
	public Result<List<PostType>> edit(@RequestBody List<PostType> postTypes) throws Exception {
		postTypeService.update(postTypes);
		return Result.OK("更新职系成功",postTypes);
	}
	
	@DeleteMapping("remove/{postTypeIds}")
	@Operation(summary = "删除职系", description = "删除职系信息", method = "DELETE")
	public Result<List<Post>> remove(@PathVariable(name = "postTypeIds") String postTypeIds) throws Exception {
		String[] postTypeId = postTypeIds.split(",");
		postTypeService.deletePostTypes(postTypeId);
		return Result.OK("删除职系成功",null);
	}

}

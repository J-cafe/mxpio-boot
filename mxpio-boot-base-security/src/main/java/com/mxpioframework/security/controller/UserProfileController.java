package com.mxpioframework.security.controller;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.JpaUtil;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.UserProfile;
import com.mxpioframework.security.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserProfileController", description = "个性化管理")
@RestController("mxpio.security.userProfileController")
@RequestMapping("/user/profile")
public class UserProfileController {
	
	@Autowired
	private UserProfileService userProfileService;

	@GetMapping("page")
	@Operation(summary = "个性化列表(分页)", description = "获取个性化列表(分页)", method = "GET")
	public Result<Page<UserProfile>> page(Criteria criteria,
								   @RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
								   @RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) throws Exception {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Page<UserProfile> page = userProfileService.listPage(UserProfile.class, pageAble, criteria);
		return Result.OK(page);
	}

	@GetMapping("list")
	@Operation(summary = "个性化列表", description = "获取个性化列表", method = "GET")
	public Result<List<UserProfile>> list(Criteria criteria) throws Exception {
		List<UserProfile> profiles = userProfileService.list(UserProfile.class, criteria);
		return Result.OK(profiles);
	}

	@GetMapping("list/page/{pageKey}")
	@Operation(summary = "个性化列表", description = "获取个性化列表", method = "GET")
	public Result<List<UserProfile>> page(@PathVariable(name = "pageKey") String pageKey) throws Exception {
		List<UserProfile> profiles = userProfileService.listByPageKey(pageKey);
		return Result.OK(profiles);
	}

	@GetMapping("list/personal")
	@Operation(summary = "个人个性化配置", description = "获取个人个性化配置列表", method = "GET")
	public Result<List<UserProfile>> personal(Criteria criteria) throws Exception {
		List<UserProfile> profiles = userProfileService.listByLoginUser();
		return Result.OK(profiles);
	}

	@PostMapping("add")
	@Operation(summary = "添加个性化", description = "添加个性化信息", method = "POST")
	public Result<List<UserProfile>> add(@RequestBody List<UserProfile> userProfiles) throws Exception {
		userProfileService.save(userProfiles);
		return Result.OK("添加个性化成功",userProfiles);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新个性化", description = "更新个性化信息", method = "PUT")
	public Result<List<UserProfile>> edit(@RequestBody List<UserProfile> userProfiles) throws Exception {
		userProfileService.update(userProfiles);
		return Result.OK("更新个性化成功",userProfiles);
	}
	
	@DeleteMapping("remove/{ids}")
	@Operation(summary = "删除个性化", description = "删除个性化信息", method = "DELETE")
	public Result<List<UserProfile>> remove(@PathVariable(name = "ids") String ids) throws Exception {
		String[] profileId = ids.split(",");
		for (String id : profileId){
			userProfileService.delete(userProfileService.getById(UserProfile.class, id));
		}
		return Result.OK("删除个性化成功",null);
	}
}

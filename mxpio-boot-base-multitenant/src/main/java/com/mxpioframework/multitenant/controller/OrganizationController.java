package com.mxpioframework.multitenant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.jpa.query.CriteriaUtils;
import com.mxpioframework.multitenant.domain.Organization;
import com.mxpioframework.multitenant.manager.service.OrganizationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "OrganizationController", tags = { "租户接口" })
@RestController("mxpio.multitenant.organizationController")
@RequestMapping("/multitenant/org/")
public class OrganizationController {
	
	@Autowired
	private OrganizationService organizationService;
	
	@GetMapping("page")
	@ApiOperation(value = "企业列表", notes = "企业列表", httpMethod = "GET")
	public Result<Page<Organization>> page(String criteria,
			@RequestParam(value="pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value="pageNo", defaultValue = "1") Integer pageNo) {
		Pageable pageAble = PageRequest.of(pageNo-1, pageSize);
		Criteria c = CriteriaUtils.json2Criteria(criteria);
		Page<Organization> page = organizationService.load(pageAble, c);
		return Result.OK(page);
	}
	
	@PostMapping("add")
	@ApiOperation(value = "新增企业", notes = "新增企业", httpMethod = "POST")
	public Result<List<Organization>> save(@RequestBody List<Organization> organizations) {
		organizationService.save(organizations);
		return Result.OK(organizations);
	}
	
	@GetMapping("check/{organizationId}")
	@ApiOperation(value = "查询企业是否存在", notes = "查询企业是否存在", httpMethod = "GET")
	public Result<Object> isExist(@PathVariable String organizationId) {
		if (organizationService.isExist(organizationId)) {
			return Result.error("公司ID已经存在。");
		}
		return Result.OK();
	}
	

}

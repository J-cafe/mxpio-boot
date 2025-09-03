package com.mxpioframework.log.controller;


import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.log.entity.MxpioLog;
import com.mxpioframework.log.service.MxpioLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "MxpioLogController", description = "日志管理")
@RestController("mxpio.log.MxpioLogController")
@RequestMapping("/log/")
public class MxpioLogController {

    @Autowired
    private MxpioLogService mxpioLogService;

    @GetMapping("list")
    @Operation(summary = "日志list", description = "日志list（无分页）", method = "GET")
    public Result<List<MxpioLog>> list(@Parameter(description = "查询参数") Criteria criteria) {
        List<MxpioLog> list = mxpioLogService.list(criteria);
        return Result.OK(list);
    }

    @GetMapping("page")
    @Operation(summary = "日志page", description = "日志page", method = "GET")
    public Result<Page<MxpioLog>> page(@Parameter(description = "查询参数") Criteria criteria, @Parameter(description = "每页条数") Integer pageSize, @Parameter(description = "页号") Integer pageNo){
        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<MxpioLog> pageList = mxpioLogService.listPage(page, criteria);
        return Result.OK(pageList);
    }

    @GetMapping("list/{id}")
    @Operation(summary = "根据id获取日志", description = "根据id获取日志", method = "GET")
    public Result<MxpioLog> getById(@Parameter(description = "id") @PathVariable(name = "id") String id) {
        MxpioLog log = mxpioLogService.getById(Long.parseLong(id));
        return Result.OK(log);
    }

}

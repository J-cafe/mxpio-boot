package com.mxpioframework.log.controller;


import com.mxpioframework.common.vo.Result;
import com.mxpioframework.log.service.LogRecordService;
import com.mxpioframework.log.vo.LogParam;
import com.mxpioframework.log.vo.LogVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MxpioLogController", description = "日志管理")
@RestController("mxpio.log.MxpioLogController")
@RequestMapping("/log/")
public class MxpioLogController {

    @Autowired
    private LogRecordService logRecordService;

    @GetMapping("page")
    @Operation(summary = "日志page", description = "日志page", method = "GET")
    public Result<Page<LogVO>> page(@Parameter(description = "查询参数") LogParam param, @Parameter(description = "每页条数") Integer pageSize, @Parameter(description = "页号") Integer pageNo){
        Pageable page = PageRequest.of(pageNo - 1, pageSize);
        Page<LogVO> pageList = logRecordService.listPage(page, param);
        return Result.OK(pageList);
    }

}

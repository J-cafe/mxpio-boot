package com.mxpioframework.module.datav.controller;

import com.mxpioframework.common.ds.DataSet;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.module.datav.service.DataSetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "DataSetController", description = "数据集接口")
@RestController("mxpio.datav.dataSetController")
@RequestMapping("/datav/dataset/")
public class DataSetController {

    @Autowired
    @Qualifier(DataSetService.BEAN_ID)
    private DataSetService dataSetService;

    @GetMapping("type/list")
    @Operation(summary = "查询数据集类型", description = "根据查询数据集类型", method = "GET")
    public Result<Map<String, String>> types() {
        return Result.OK(dataSetService.getTypes());
    }

    @GetMapping("list/{type}")
    @Operation(summary = "查询数据集", description = "根据数据集类型查询数据集", method = "GET")
    public Result<List<? extends DataSet>> list(@PathVariable("type") String type) {
        return Result.OK(dataSetService.getDataSets(type));
    }

    @GetMapping("list/{type}/{code}")
    @Operation(summary = "查询数据集", description = "根据数据集类型和Code查询数据集", method = "GET")
    public Result<DataSet> getOne(@PathVariable("type") String type,
            @PathVariable("code") String code) {
        return Result.OK(dataSetService.getDataSet(type, code));
    }

    @GetMapping("data/{type}/{code}")
    @Operation(summary = "查询数据集数据", description = "根据数据集类型和Code查询数据集数据", method = "GET")
    public Result<List<?>> getData(@PathVariable("type") String type,
            @PathVariable("code") String code) {
        return Result.OK(dataSetService.getData(type, code));
    }

    @GetMapping("paging/{type}/{code}")
    @Operation(summary = "查询数据集数据(分页)", description = "根据数据集类型和Code查询数据集数据(分页)", method = "GET")
    public Result<Page<?>> pagingData(@PathVariable("type") String type,
                  @PathVariable("code") String code,
                  @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                  @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
        Pageable pageAble = PageRequest.of(pageNo - 1, pageSize);
        return Result.OK(dataSetService.pagingData(type, code, pageAble));
    }

    @PostMapping("add/{type}")
    @Operation(summary = "新增数据集", description = "新增数据集", method = "POST")
    public Result<DataSet> add(@PathVariable("type") String type,
                               @Parameter(description="参照DbDataSet的序列号json传入") @RequestBody String json) {

        return Result.OK(dataSetService.save(type, json));
    }

    @PutMapping("edit/{type}")
    @Operation(summary = "编辑数据集", description = "编辑数据集（全量）", method = "PUT")
    public Result<DataSet> edit(@PathVariable("type") String type,
                                @Parameter(description="参照DbDataSet的序列号json传入") @RequestBody String json) {
        return Result.OK(dataSetService.update(type, json));
    }

    @DeleteMapping("remove/{type}/{code}")
    @Operation(summary = "删除数据集", description = "删除数据集", method = "DELETE")
    public Result<DataSet> remove(@PathVariable("type") String type,
                                  @PathVariable(name = "code") String code) {
        dataSetService.delete(type, code);
        return Result.OK();
    }


}

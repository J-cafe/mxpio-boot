package ${genSystem.rootPackage}.${genModel.packageName}.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ${genSystem.rootPackage}.${genModel.packageName}.entity.*;
import ${genSystem.rootPackage}.${genModel.packageName}.service.*;
import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "${genModel.modelCode}Controller", description = "${genModel.modelName}接口")
@RestController
@RequestMapping("/${genSystem.systemPath}/${genModel.modelPath}/")
public class ${genModel.modelCode}Controller {
	@Autowired
	private ${genModel.modelCode}Service ${genModel.modelCode?uncap_first}Serivce;

	@GetMapping("list")
	@Operation(summary = "${genModel.modelName}列表", description = "获取${genModel.modelName}列表", method = "GET")
	public Result<List<${genModel.modelCode}>> list(Criteria criteria) throws UnsupportedEncodingException {
		List<${genModel.modelCode}> items = ${genModel.modelCode?uncap_first}Serivce.list(${genModel.modelCode}.class, criteria);
		return Result.OK(items);
	}
	
	@GetMapping("page")
	@Operation(summary = "${genModel.modelName}列表", description = "获取${genModel.modelName}列表", method = "GET")
	public Result<Page<${genModel.modelCode}>> page(Criteria criteria, Integer pageSize, Integer pageNo) throws UnsupportedEncodingException {
		Pageable page = PageRequest.of(pageNo-1, pageSize);
		Page<${genModel.modelCode}> items = ${genModel.modelCode?uncap_first}Serivce.listPage(${genModel.modelCode}.class, page, criteria);
		return Result.OK(items);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "根据id获取${genModel.modelName}", description = "根据id获取${genModel.modelName}", method = "GET")
	public Result<${genModel.modelCode}> getById(@PathVariable(name = "id", required = true) String id) {
		${genModel.modelCode} item = ${genModel.modelCode?uncap_first}Serivce.getById(${genModel.modelCode}.class, id);
		return Result.OK(item);
	}

	@PostMapping("add")
	@Operation(summary = "新增${genModel.modelName}", description = "新增${genModel.modelName}", method = "POST")
	public Result<${genModel.modelCode}> add(@RequestBody ${genModel.modelCode} ${genModel.modelCode?uncap_first}) {
 		${genModel.modelCode?uncap_first}Serivce.save(${genModel.modelCode?uncap_first});
		return Result.OK(${genModel.modelCode?uncap_first});
	}
	
	@PutMapping("edit")
	@Operation(summary = "编辑${genModel.modelName}", description = "编辑${genModel.modelName}（全量）", method = "PUT")
	public Result<${genModel.modelCode}> edit(@RequestBody ${genModel.modelCode} ${genModel.modelCode?uncap_first}) {
		${genModel.modelCode?uncap_first}Serivce.update(${genModel.modelCode?uncap_first});
		return Result.OK(${genModel.modelCode?uncap_first});
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除${genModel.modelName}", description = "删除${genModel.modelName}", method = "DELETE")
	public Result<${genModel.modelCode}> remove(@PathVariable(name = "id", required = true) String id) {
		String ids[] = id.split(",");
		for(String key : ids){
			${genModel.modelCode?uncap_first}Serivce.delete(${genModel.modelCode}.class, key);
		}
		return Result.OK();
	}
}
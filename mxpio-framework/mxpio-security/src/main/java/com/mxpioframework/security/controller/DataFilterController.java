package com.mxpioframework.security.controller;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mxpioframework.security.util.SecurityUtils;
import com.mxpioframework.security.vo.FieldInfo;
import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.jpa.query.Criteria;
import com.mxpioframework.security.entity.DataFilter;
import com.mxpioframework.security.service.DataFilterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DataFilterController", description = "数据过滤管理")
@RestController("mxpio.security.dataFilterController")
@RequestMapping("/datafilter/")
public class DataFilterController {
	
	@Autowired
	private DataFilterService dataFilterService;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	@GetMapping("list")
	@Operation(summary = "数据过滤列表", description = "获取数据过滤列表", method = "GET")
	public Result<List<DataFilter>> page(Criteria criteria) throws Exception {
		List<DataFilter> dataFilters = dataFilterService.list(criteria);
		return Result.OK(dataFilters);
	}
	
	@GetMapping("list/{id}")
	@Operation(summary = "获取数据过滤", description = "根据ID获取数据过滤", method = "GET")
	public Result<DataFilter> getById(@PathVariable(name = "id", required = true) String id) throws Exception {
		DataFilter dataFilter = dataFilterService.getById(id);
		return Result.OK(dataFilter);
	}
	
	@GetMapping("res/list/{resId}")
	@Operation(summary = "获取数据过滤", description = "根据resId获取数据过滤", method = "GET")
	public Result<List<DataFilter>> getByResourceId(@PathVariable(name = "resId", required = true) String resId) throws Exception {
		List<DataFilter> dataFilters = dataFilterService.getByResourceId(resId);
		return Result.OK(dataFilters);
	}
	
	@PostMapping("add")
	@Operation(summary = "添加数据过滤", description = "添加数据过滤信息", method = "POST")
	public Result<DataFilter> add(@RequestBody DataFilter dataFilter) throws Exception {
		dataFilterService.save(dataFilter);
		return Result.OK("添加成功",dataFilter);
	}
	
	@PutMapping("edit")
	@Operation(summary = "更新数据过滤", description = "更新数据过滤信息", method = "PUT")
	public Result<DataFilter> edit(@RequestBody DataFilter dataFilter) throws Exception {
		dataFilterService.save(dataFilter);
		return Result.OK("编辑成功",dataFilter);
	}
	
	@DeleteMapping("remove/{id}")
	@Operation(summary = "删除数据过滤", description = "根据数据过滤名id删除数据过滤信息", method = "DELETE")
	public Result<DataFilter> delete(@PathVariable(name = "id", required = true) String id) throws Exception {
		String[] ids = id.split(",");
		for(String key : ids){
			dataFilterService.delete(dataFilterService.getById(key));
		}
		return Result.OK("删除成功",null);
	}

	@GetMapping("datascope/servers")
	@Operation(summary = "服务列表", description = "数据权限过滤服务列表", method = "GET")
	public Result<Set<String>> servers() {
		Set<String> servers = SecurityUtils.getDataScapeProviderMap().keySet();
		return Result.OK(servers);
	}

	@GetMapping("fields")
	@Operation(summary = "获取接口实体字段列表", description = "根据接口URL路径获取返回实体对象的字段列表，用于前端配置字段数据过滤", method = "GET")
	public Result<List<FieldInfo>> getEntityFields(@RequestParam("url") String url) {
		Map<RequestMappingInfo, HandlerMethod> handlerMethods = requestMappingHandlerMapping.getHandlerMethods();
		HandlerMethod targetMethod = null;
		for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
			Set<String> patterns = entry.getKey().getPatternValues();
			if (patterns != null && patterns.contains(url)) {
				targetMethod = entry.getValue();
				break;
			}
		}

		if (targetMethod == null) {
			return Result.error("未找到匹配的接口: " + url);
		}

		MethodParameter[] parameters = targetMethod.getMethodParameters();
		boolean hasCriteria = false;
		for(MethodParameter parameter : parameters){
			if(Criteria.class.equals(parameter.getExecutable().getParameterTypes()[parameter.getParameterIndex()])){
				hasCriteria = true;
				break;
			}
		}
		if (!hasCriteria){
			return Result.error("未找到接口上的Criteria参数: " + url+"，当前接口不可过滤，不能配置过滤字段！");
		}

		Method method = targetMethod.getMethod();
		Class<?> entityClass = extractEntityClass(method.getGenericReturnType());
		if (entityClass == null) {
			return Result.error("无法从接口返回类型中提取实体类: " + method.getReturnType().getName());
		}
		List<FieldInfo> fieldInfos = extractFieldInfos(entityClass);
		return Result.OK(fieldInfos);
	}

	/**
	 * 从方法返回类型中提取实体Class
	 * 支持: Result<Page<X>>, Result<List<X>>, Result<Collection<X>>, Result<X>
	 */
	private Class<?> extractEntityClass(Type returnType) {
		if (!(returnType instanceof ParameterizedType)) {
			return null;
		}
		ParameterizedType resultType = (ParameterizedType) returnType;
		Type[] resultArgs = resultType.getActualTypeArguments();
		if (resultArgs.length == 0) {
			return null;
		}
		Type innerType = resultArgs[0];
		// 如果内层是 Page/List/Collection 等泛型类型，再提取一层
		if (innerType instanceof ParameterizedType) {
			ParameterizedType innerParamType = (ParameterizedType) innerType;
			Class<?> rawType = (Class<?>) innerParamType.getRawType();
			if (Page.class.isAssignableFrom(rawType) || Collection.class.isAssignableFrom(rawType)) {
				Type[] innerArgs = innerParamType.getActualTypeArguments();
				if (innerArgs.length > 0 && innerArgs[0] instanceof Class) {
					return (Class<?>) innerArgs[0];
				}
			}
			return null;
		}
		// 内层直接是Class，如 Result<X>
		if (innerType instanceof Class) {
			return (Class<?>) innerType;
		}
		return null;
	}

	/**
	 * 反射提取实体类字段信息（含父类字段，直到Object为止）
	 */
	private List<FieldInfo> extractFieldInfos(Class<?> clazz) {
		List<FieldInfo> fieldInfos = new ArrayList<>();
		Class<?> current = clazz;
		while (current != null && current != Object.class) {
			for (Field field : current.getDeclaredFields()) {
				// 跳过静态字段和serialVersionUID
				if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				//跳过字段名称为crudType、saveTransient、textMap
				if ("crudType".equals(field.getName()) || "saveTransient".equals(field.getName()) || "textMap".equals(field.getName())) {
					continue;
				}
				//跳过 @Transient 注解字段
				if (field.isAnnotationPresent(Transient.class)) {
					continue;
				}
				//跳过返回类型为List/map等集合类型的字段
				if (field.getType().isAssignableFrom(List.class) || field.getType().isAssignableFrom(Map.class)) {
					continue;
				}
				String description = "";
				Schema schema = field.getAnnotation(Schema.class);
				if (schema != null) {
					description = schema.description();
				}
				fieldInfos.add(new FieldInfo(field.getName(), field.getType().getSimpleName(), description));
			}
			current = current.getSuperclass();
		}
		return fieldInfos;
	}
}

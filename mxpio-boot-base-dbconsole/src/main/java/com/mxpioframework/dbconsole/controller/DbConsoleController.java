package com.mxpioframework.dbconsole.controller;

import java.sql.BatchUpdateException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mxpioframework.common.vo.Result;
import com.mxpioframework.dbconsole.entity.DbInfo;
import com.mxpioframework.dbconsole.manager.IConsoleDbInfoManager;
import com.mxpioframework.dbconsole.model.ColumnInfo;
import com.mxpioframework.dbconsole.model.DataGridWrapper;
import com.mxpioframework.dbconsole.model.ProcInfo;
import com.mxpioframework.dbconsole.model.TableInfo;
import com.mxpioframework.dbconsole.service.DbService;
import com.mxpioframework.dbconsole.service.ISqlWrapperService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "DbConsoleController", description = "数据库接口")
@RestController("mxpio.dbconsole.dbConsoleController")
@RequestMapping("/dbconsole/")
public class DbConsoleController {

	@Autowired
	@Qualifier(DbService.BEAN_ID)
	private DbService dbService;
	
	@Autowired
	@Qualifier(ISqlWrapperService.BEAN_ID)
	private ISqlWrapperService sqlWrapperService;
	
	@Autowired
	@Qualifier(IConsoleDbInfoManager.BEAN_ID)
	private IConsoleDbInfoManager consoleDbInfoManager;

	@GetMapping("db/list")
	@Operation(summary = "查询数据库连接信息", description = "根据登录用户查询数据库连接信息", method = "GET")
	public Result<Collection<DbInfo>> loadDbInfos() {
		try {
			return Result.OK(dbService.findDbInfos());
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
	}

	@GetMapping("table/list/{dbInfoId}")
	@Operation(summary = "查询数据库表信息", description = "根据数据库ID查询表信息", method = "GET")
	public Result<Collection<TableInfo>> loadTableInfos(@PathVariable("dbInfoId") String dbInfoId) {
		try {
			return Result.OK(dbService.findTableInfos(dbInfoId));
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
	}
	
	@GetMapping("view/list/{dbInfoId}")
	@Operation(summary = "查询数据库视图信息", description = "根据数据库ID查询视图信息", method = "GET")
	public Result<Collection<TableInfo>> loadViewInfos(@PathVariable("dbInfoId") String dbInfoId) {
		try {
			return Result.OK(dbService.findViewInfos(dbInfoId));
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
	}
	
	@GetMapping("proc/list/{dbInfoId}")
	@Operation(summary = "查询数据库存储过程信息", description = "根据数据库ID查询存储过程信息", method = "GET")
	public Result<Collection<ProcInfo>> loadProcInfos(@PathVariable("dbInfoId") String dbInfoId) {
		try {
			return Result.OK(dbService.findProcInfos(dbInfoId));
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
	}

	@GetMapping("column/list/{dbInfoId}/{tableName}")
	@Operation(summary = "查询字段信息", description = "根据数据库ID和表名查询字段信息", method = "GET")
	public Result<Collection<ColumnInfo>> loadColumnInfos(@PathVariable("dbInfoId") String dbInfoId, @PathVariable("tableName") String tableName) {
		try {
			return Result.OK(dbService.findColumnInfos(dbInfoId, tableName));
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
	}

	@GetMapping("data/page/{dbInfoId}")
	@Operation(summary = "查询数据", description = "查询数据", method = "GET")
	public Result<Page<Map<String, Object>>> loadQueryTableData(@PathVariable("dbInfoId") String dbInfoId,
			@RequestParam(value = "sql", required = false) String sql, 
			@RequestParam(value = "tableName", required = false) String tableName,
			@RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
			@RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo) {
		Pageable pageAble = PageRequest.of(pageNo - 1, pageSize);
		DataGridWrapper tableData;
		try {
			tableData = dbService.queryTableData(dbInfoId, tableName, sql, pageAble.getPageSize(),
					pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("查询失败：" + e.getMessage());
		}
		return Result.OK(new PageImpl<Map<String, Object>>(tableData.getTableData(), pageAble, tableData.getTotalCount()));
	}

	@PostMapping("db/add")
	@Operation(summary = "新增数据库连接信息", description = "新增数据库连接信息", method = "POST")
	public Result<DbInfo> add(@RequestBody DbInfo dbInfo) {
		try {
			consoleDbInfoManager.insertDbInfo(dbInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("新增失败：" + e.getMessage());
		}
		return Result.OK(dbInfo);
	}
	
	@PutMapping("db/edit")
	@Operation(summary = "编辑数据库连接信息", description = "编辑数据库连接信息", method = "PUT")
	public Result<DbInfo> edit(@RequestBody DbInfo dbInfo) {
		try {
			consoleDbInfoManager.updateDbInfo(dbInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("编辑失败：" + e.getMessage());
		}
		return Result.OK(dbInfo);
	}
	
	@DeleteMapping("db/remove/{id}")
	@Operation(summary = "删除数据库连接信息", description = "删除数据库连接信息", method = "DELETE")
	public Result<DbInfo> remove(@PathVariable(name = "id", required = true) String id) {
		try {
			consoleDbInfoManager.deleteDbInfoById(id);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败：" + e.getMessage());
		}
		return Result.OK();
	}
	
	/**
	 * 测试连接是否成功
	 * @param map
	 * @return
	 */
	@PostMapping("db/test")
	@Operation(summary = "测试连接", description = "测试连接(url\\class\\username\\password必填)", method = "POST")
	public Result<Object> testConnection(@RequestBody DbInfo dbInfo) {
		String errorMsg = dbService.checkDbConnection(dbInfo);
		if(errorMsg == null){
			return Result.OK();
		}else{
			return Result.error(errorMsg);
		}
		
	}
	
	@PostMapping("column/add/{dbInfoId}/{tableName}")
	@Operation(summary = "新增表字段", description = "新增表字段", method = "POST")
	public Result<ColumnInfo> addColumn(@PathVariable("dbInfoId") String dbInfoId,
			@PathVariable("tableName") String tableName,
			@RequestBody ColumnInfo columnInfo) {
		columnInfo.setTableName(tableName);
		try {
			dbService.insertColumn(dbInfoId, columnInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("新增失败：" + e.getMessage());
		}
		return Result.OK();
	}
	
	@PutMapping("column/edit/{dbInfoId}/{tableName}")
	@Operation(summary = "编辑表字段", description = "编辑表字段", method = "PUT")
	public Result<ColumnInfo> editColumn(@PathVariable("dbInfoId") String dbInfoId,
			@PathVariable("tableName") String tableName,
			@RequestBody ColumnInfo columnInfo) {
		columnInfo.setTableName(tableName);
		return Result.OK();
	}
	
	@DeleteMapping("column/remove/{dbInfoId}/{tableName}/{columnName}")
	@Operation(summary = "删除表字段", description = "删除表字段", method = "DELETE")
	public Result<DbInfo> removeColumn(@PathVariable("dbInfoId") String dbInfoId,
			@PathVariable("tableName") String tableName,
			@PathVariable("columnName") String columnName) {
		try {
			dbService.deleteColumn(dbInfoId, tableName, columnName);
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败：" + e.getMessage());
		}
		return Result.OK();
	}

	/*@SuppressWarnings("unchecked")
	@DataResolver
	public void saveTableColumn(Collection<ColumnInfo> coll, Map<String, Object> map) throws Exception {
		String dbInfoId = (String) map.get("dbInfoId");
		String tableName = (String) map.get("tableName");
		for (Iterator<ColumnInfo> iter = EntityUtils.getIterator(coll, FilterType.ALL); iter.hasNext();) {
			ColumnInfo columnInfo = iter.next();
			columnInfo.setTableName(tableName);
			EntityState state = EntityUtils.getState(columnInfo);
			if (state.equals(EntityState.NEW)) {
				dbService.insertColumn(dbInfoId, columnInfo);
			}
			if (state.equals(EntityState.MODIFIED)) {
				EntityEnhancer entityEnhancer = EntityUtils.getEntityEnhancer(columnInfo);
				String oldDefaultValue = null;
				if (entityEnhancer != null) {
					Map<String, Object> oldValues = entityEnhancer.getOldValues();
					if (oldValues.get("defaultValue") != null) {
						oldDefaultValue = (String) oldValues.get("defaultValue");
					}
				}
				ColumnInfo oldColumnInfo = new ColumnInfo();
				BeanUtils.copyProperties(oldColumnInfo, columnInfo);
				oldColumnInfo.setColumnName(EntityUtils.getOldString(columnInfo, "columnName"));
				oldColumnInfo.setIsnullAble(EntityUtils.getOldBoolean(columnInfo, "isnullAble"));
				oldColumnInfo.setIsprimaryKey(EntityUtils.getOldBoolean(columnInfo, "isprimaryKey"));
				oldColumnInfo.setDefaultValue(oldDefaultValue);
				dbService.updateColumn(dbInfoId, oldColumnInfo, columnInfo);

			}
			if (state.equals(EntityState.DELETED)) {
				dbService.deleteColumn(dbInfoId, tableName, columnInfo.getColumnName());
			}
		}

	}

	@SuppressWarnings("unchecked")
	@DataResolver
	public void saveTableData(Collection<Map<String, Object>> coll, Map<String, Object> map) throws Exception {
		String dbInfoId = (String) map.get("dbInfoId");
		String tableName = (String) map.get("tableName");
		List<SqlWrapper> listSqlWrapper = new ArrayList<SqlWrapper>();
		for (Iterator<Map<String, Object>> iter = EntityUtils.getIterator(coll, FilterType.ALL); iter.hasNext();) {
			Map<String, Object> mapValues = iter.next();
			EntityState state = EntityUtils.getState(mapValues);
			if (state.equals(EntityState.NEW)) {
				listSqlWrapper.add(sqlWrapperService.getInsertTableSql(tableName, mapValues));
			}
			if (state.equals(EntityState.MODIFIED)) {
				Map<String, Object> oldMapValues = this.getTableOldMapValues(mapValues);
				listSqlWrapper.add(sqlWrapperService.getUpdateTableSql(tableName, mapValues, oldMapValues));
			}
			if (state.equals(EntityState.DELETED)) {
				Map<String, Object> oldMapValues = this.getTableOldMapValues(mapValues);
				listSqlWrapper.add(sqlWrapperService.getDeleteTableSql(tableName, oldMapValues));
			}
		}
		if (listSqlWrapper.size() > 0) {
			dbService.updateSql(dbInfoId, listSqlWrapper);
		}

	}

	private Map<String, Object> getTableOldMapValues(Map<String, Object> mapValues) {
		EntityEnhancer entityEnhancer = EntityUtils.getEntityEnhancer(mapValues);
		if (entityEnhancer != null) {
			Map<String, Object> oldValues = entityEnhancer.getOldValues();
			if (oldValues != null) {
				return oldValues;
			} else {
				return mapValues;
			}
		}
		return null;
	}*/

	@GetMapping("db/types")
	@Operation(summary = "查询全部数据库类型", description = "查询全部数据库类型", method = "GET")
	public Result<Collection<DbInfo>> loadDbTypes() {
		List<String> list = dbService.loadDbTypes();
		Collection<DbInfo> dbInfoList = new ArrayList<DbInfo>();
		DbInfo dbInfo = null;
		for (String s : list) {
			dbInfo = new DbInfo();
			dbInfo.setDbType(s);
			dbInfoList.add(dbInfo);
		}
		return Result.OK(dbInfoList);
	}

	@PostMapping("data/update/{dbInfoId}")
	@Operation(summary = "执行更新SQL", description = "执行更新SQL", method = "POST")
	public Result<Object> executeUpdateSql(@PathVariable("dbInfoId") String dbInfoId, @RequestBody String sql) {
		String[] sqls = sql.split(";");
		int[] ints;
		try {
			ints = dbService.updateSql(dbInfoId, sqls);
		} catch (BatchUpdateException e) {
			return Result.error(e.getMessage());
		} catch (Exception e) {
			return Result.error(e.getMessage());
		}
		return Result.OK(ints);
	}

	@GetMapping("column/type/{dbInfoId}")
	@Operation(summary = "查询全部字段类型", description = "根据数据库ID查询全部字段类型", method = "GET")
	public Result<List<ColumnInfo>> findColumnType(@PathVariable("dbInfoId") String dbInfoId) {
		if (StringUtils.hasText(dbInfoId)) {
			List<ColumnInfo> list = new ArrayList<ColumnInfo>();
			ColumnInfo info = null;
			List<String> types = null;
			try {
				types = dbService.findDefaultColumnType(dbInfoId);
			} catch (Exception e) {
				e.printStackTrace();
				return Result.error("查询失败：" + e.getMessage());
			}
			for (String s : types) {
				info = new ColumnInfo();
				info.setColumnType(s);
				list.add(info);
			}
			return Result.OK(list);
		}
		return Result.error("数据库ID为空！");
	}

	@DeleteMapping("table/remove/{dbInfoId}/{tableName}")
	@Operation(summary = "删除表", description = "根据数据库ID和表名删除表", method = "DELETE")
	public Result<Object> deleteTable(@PathVariable("dbInfoId") String dbInfoId,@PathVariable("tableName") String tableName) {
		try {
			dbService.deleteTable(dbInfoId, tableName);
			return Result.OK();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败："+e.getMessage());
		}
	}

	@PutMapping("table/alert/{dbInfoId}/{tableName}/{newTableName}")
	@Operation(summary = "修改表名", description = "根据数据库ID和表名修改表名", method = "PUT")
	public Result<Object> alertTableName(@PathVariable("dbInfoId") String dbInfoId, @PathVariable("tableName") String tableName, @PathVariable("newTableName") String newTableName) {
		try {
			dbService.alertTableName(dbInfoId, tableName, newTableName);
			return Result.OK();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("修改失败："+e.getMessage());
		}
	}

	@PostMapping("table/create/{dbInfoId}/{tableName}")
	@Operation(summary = "创建表", description = "根据数据库ID和表名创建表", method = "POST")
	public Result<Object> createTable(@PathVariable("dbInfoId") String dbInfoId, @PathVariable("tableName") String tableName) {
		try {
			dbService.createTable(dbInfoId, tableName);
			return Result.OK();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("创建失败："+e.getMessage());
		}
	}

	@DeleteMapping("data/remove/{dbInfoId}/{tableName}")
	@Operation(summary = "删除表数据", description = "根据数据库ID和表名删除表数据", method = "DELETE")
	public Result<Object> deleteTableData(@PathVariable("dbInfoId") String dbInfoId, @PathVariable("tableName") String tableName) {
		try {
			dbService.deleteTableData(dbInfoId, tableName);
			return Result.OK();
		} catch (Exception e) {
			e.printStackTrace();
			return Result.error("删除失败："+e.getMessage());
		}
	}

}

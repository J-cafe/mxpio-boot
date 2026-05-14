import { defHttp } from '@mxpio/request';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  DB = '/dbconsole/db/list',
  Table = '/dbconsole/table/list/',
  Proc = '/dbconsole/proc/list/',
  DeleteDB = '/dbconsole/db/remove/',
  DeleteTable = '/dbconsole/table/remove/',
  DeleteData = '/dbconsole/data/remove/',
  AddDb = '/dbconsole/db/add',
  EditDb = '/dbconsole/db/edit',
  DBTypes = '/dbconsole/db/types',
  DBTest = '/dbconsole/db/test',
  AddTable = '/dbconsole/table/create/',
  EditTable = '/dbconsole/table/alert/',
  Column = '/dbconsole/column/list/',
  ColumnType = '/dbconsole/column/type/',
  AddColumn = '/dbconsole/column/add/',
  EditColumn = '/dbconsole/column/edit/',
  DeleteColumn = '/dbconsole/column/remove/',
  DataList = '/dbconsole/data/page/',
}

/**
 * @description: 数据库连接列表
 */

export const dbList = (params?: RequestCriteriaParams) => {
  return defHttp.get({ url: Api.DB, params });
};

/**
 * @description: 数据库表信息
 */

export const tableList = (dbInfoId: string) => {
  return defHttp.get({ url: Api.Table + dbInfoId });
};

/**
 * @description: 数据库表数据信息
 */

export const dataList = (dbInfoId: string, params?: RequestCriteriaParams) => {
  return defHttp.get({ url: Api.DataList + dbInfoId, params });
};

/**
 * @description: 数据库表信息
 */

export const columnList = (dbInfoId: string, tableName: string, params?: RequestCriteriaParams) => {
  return defHttp.get({ url: Api.Column + dbInfoId + '/' + tableName, params });
};

/**
 * @description: 数据库存储过程
 */

export const procList = (dbInfoId: string) => {
  return defHttp.get({ url: Api.Proc + dbInfoId });
};

/**
 * @description: 删除数据库连接
 */

export function deleteDB(id: string) {
  return defHttp.delete({
    url: Api.DeleteDB + id,
  });
}

/**
 * @description: 删除数据库表
 */

export function deleteTable(dbInfoId: string, tableName: string) {
  return defHttp.delete({
    url: Api.DeleteTable + dbInfoId + '/' + tableName,
  });
}

/**
 * @description: 删除数据库表数据
 */

export function deleteTableData(dbInfoId: string, tableName: string) {
  return defHttp.delete({
    url: Api.DeleteTable + dbInfoId + '/' + tableName,
  });
}

/**
 * @description: 新增数据库连接
 */

export const addDB = (params: Recordable) => {
  return defHttp.post({ url: Api.AddDb, params });
};

/**
 * @description: 编辑数据库连接
 */

export function editDB(params: Recordable) {
  return defHttp.put({ url: Api.EditDb, params });
}

/**
 * @description: 数据库类型
 */

export const dbTypesList = () => {
  return defHttp.get({ url: Api.DBTypes });
};

/**
 * @description: 数据库字段类型
 */

export const columnTypeList = (dbInfoId: string) => {
  return defHttp.get({ url: Api.ColumnType + dbInfoId });
};

/**
 * @description: 数据库连接测试
 */

export const dbTest = (params: Recordable) => {
  return defHttp.post({ url: Api.DBTest, params });
};

/**
 * @description: 新增数据表
 */

export const addTable = (dbInfoId: string, tableName: string) => {
  return defHttp.post({ url: Api.AddTable + dbInfoId + '/' + tableName });
};

/**
 * @description: 编辑数据表
 */

export function editTable(dbInfoId: string, oldTableName: string, tableName: string) {
  return defHttp.put({ url: Api.EditTable + dbInfoId + '/' + oldTableName + '/' + tableName });
}

/**
 * @description: 新增数据表字段
 */

export const addColumn = (dbInfoId: string, tableName: string, params) => {
  return defHttp.post({ url: Api.AddColumn + dbInfoId + '/' + tableName, params });
};

/**
 * @description: 编辑数据表字段
 */

export function editColumn(dbInfoId: string, tableName: string, params) {
  return defHttp.put({ url: Api.EditColumn + dbInfoId + '/' + tableName, params });
}

/**
 * @description: 删除数据库表
 */

export function deleteColumn(dbInfoId: string, tableName: string, columnName: string) {
  return defHttp.delete({
    url: Api.DeleteColumn + dbInfoId + '/' + tableName + '/' + columnName,
  });
}

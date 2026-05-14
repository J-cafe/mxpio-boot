import { useBridge } from '@mxpio/bridge';
import { UploadFileParams } from '@mxpio/types';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/excel/import/list',
  Add = '/excel/import/add',
  Edit = '/excel/import/edit',
  Delete = '/excel/import/remove/',
  ImportFactoryList = '/excel/import/factory/list',
  Create = '/excel/import/rule/mapping/create',
  RuleDelete = '/excel/import/rule/remove/',
  PreList = '/excel/import/parser/pre/list',
  PostList = '/excel/import/parser/post/list',
  RuleAdd = '/excel/import/rule/add',
  RuleEdit = '/excel/import/rule/edit',
  ImportUpload = '/excel/import/upload/',
}

/**
 * @description: Get Import list based
 */

export const importList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Import based
 */

export const addImport = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Import based
 */

export function editImport(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Import based
 */

export function deleteImport(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 查询数据源列表
 */

export const importFactoryList = () => {
  const { http } = useBridge();
  return http.get({ url: Api.ImportFactoryList });
};

/**
 * @description: 查询实体类列表
 */

export const entityClassList = (factory: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.ImportFactoryList}/${factory}/list` });
};

/**
 * @description: 生成规则映射
 */

export const createMappingRuleList = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Create, params });
};

/**
 * @description: delete Import rule based
 */

export function deleteRule(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.RuleDelete + ids,
  });
}

/**
 * @description: 前置解析器
 */

export const preList = () => {
  const { http } = useBridge();
  return http.get({ url: Api.PreList });
};

/**
 * @description: 后置解析器
 */

export const parserPostList = () => {
  const { http } = useBridge();
  return http.get({ url: Api.PostList });
};

/**
 * @description: Add Import rule  based
 */

export const addRule = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.RuleAdd, params });
};

/**
 * @description: Edit Import rule  based
 */

export function editRule(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.RuleEdit, params });
}

/**
 * @description: 导入文件方法
 * @param code 导入方案编码。
 * @param file 导入文件。
 */
export const importUpload = (code: String, file: UploadFileParams) => {
  const { http } = useBridge();
  return http.uploadFile({ url: `${Api.ImportUpload}${code}`, timeout: 5 * 60 * 1000 }, file);
};

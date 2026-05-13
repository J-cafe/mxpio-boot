import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inventory/inventory/list',
  Page = '/erp/inventory/inventory/page',
  Delete = '/erp/inventory/inventory/remove/',
  Config = '/erp/common/res/list/Inventory',
  Save = '/erp/inventory/inventory/save',
  InvlinePage = '/erp/inventory/inventoryitem/page',
  SaveInvline = '/erp/inventory/inventory/saveInventoryItmes',
  Commit = '/erp/inventory/inventory/commit/',
  Close = '/erp/inventory/inventory/close/',
  Submit = '/erp/inventory/inventory/submitInventory/',
  Review = '/erp/inventory/inventory/review/',
  AddLine = '/erp/inventory/inventoryitem/add',
  RemoveLine = '/erp/inventory/inventoryitem/remove/',
}

/**
 * @description: 盘点列表
 */

export const invTaskListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 盘点分页
 */

export const invTaskPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 盘点行分页
 */

export const invlinePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.InvlinePage, params });
};

/**
 * @description: 删除盘点
 */

export function deleteInvTaskApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 盘点配置
 */

export const invTaskConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存盘点明细
 */

export const saveInvLineApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.SaveInvline, params });
};

/**
 * @description: 保存盘点
 */

export const saveInvTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const commitInvTaskApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Commit + code });
};

/**
 * @description: 提交复核
 */
export const submitInvTaskApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Submit + code });
};

/**
 * @description: 复核
 */

export const reviewInvTaskApi = (code: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Review + code, params });
};

/**
 * @description: 关闭
 */
export const closeInvTaskApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + code });
};

/**
 * @description: 新增盘点明细
 */

export const addInvLineApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddLine, params });
};

/**
 * @description: 删除盘点明细
 */

export function removeInvLineApi(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.RemoveLine + ids,
  });
}

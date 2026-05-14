// 盘点计划列表
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Page = '/erp/inventory/invplan/page',
  Delete = '/erp/inventory/invplan/remove/',
  Add = '/erp/inventory/invplan/add',
  Edit = '/erp/inventory/invplan/edit',
  Enable = '/erp/inventory/invplan/enable/',
  Disable = '/erp/inventory/invplan/disable/',
}

export const invplanPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除盘点计划
 */

export function deleteInvplanApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增盘点计划
 */

export const addInvplanApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑盘点计划
 */

export function editInvplanApi(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

export const enableInvplanApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Enable + code });
};

export const disableInvplanApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Disable + code });
};

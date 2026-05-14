import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/eqp/list',
  Page = '/erp/equipment/eqp/page',
  Delete = '/erp/equipment/eqp/remove/',
  Add = '/erp/equipment/eqp/add',
  Edit = '/erp/equipment/eqp/edit',
  ListSub = '/erp/equipment/eqp/listsub/',
}

/**
 * @description: 设备台账列表
 */
export const eqpBasicsListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备台账列表
 */
export const eqpBasicsPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export function eqpBasicsSubApi(basicsCode: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.ListSub + basicsCode,
  });
}

/**
 * @description: 删除设备台账
 */
export function deleteEqpBasicsApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 新增设备台账
 */
export const addEqpBasicsApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 保存设备台账
 */
export const editEqpBasicsApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

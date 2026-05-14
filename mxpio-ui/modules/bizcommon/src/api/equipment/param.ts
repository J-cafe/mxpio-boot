import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/parameters/list',
  Page = '/erp/equipment/parameters/page',
  Delete = '/erp/equipment/parameters/remove/',
  Add = '/erp/equipment/parameters/add',
  Edit = '/erp/equipment/parameters/edit',
  DetailList = '/erp/equipment/parameters/detail/list/',
}

/**
 * @description: 设备公共参数列表
 */
export const eqpParamList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备公共参数列表
 */
export const eqpParamPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备公共参数
 */
export function deleteEqpParam(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 获取设备公共参数成员
 */
export function getEqpParamDetailList(id: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.DetailList + id,
  });
}

/**
 * @description: 保存设备公共参数
 */

export const addEqpParam = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpParam = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

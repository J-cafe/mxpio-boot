import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/eqp/sop/list',
  Page = '/erp/equipment/eqp/sop/page',
  Delete = '/erp/equipment/eqp/sop/remove/',
  Add = '/erp/equipment/eqp/sop/add',
  Edit = '/erp/equipment/eqp/sop/edit',
}

/**
 * @description: 设备资产档案列表
 */

export const eqpSopListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备资产档案列表
 */

export const eqpSopPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备资产档案
 */

export function deleteEqpSopApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备资产档案
 */

export const addEqpSopApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpSopApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

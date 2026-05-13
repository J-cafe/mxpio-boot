import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/eqp/attr/list',
  Page = '/erp/equipment/eqp/attr/page',
  Delete = '/erp/equipment/eqp/attr/remove/',
  Add = '/erp/equipment/eqp/attr/add',
  Edit = '/erp/equipment/eqp/attr/edit',
}

/**
 * @description: 设备资产档案列表
 */

export const eqpAttrListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备资产档案列表
 */

export const eqpAttrPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备资产档案
 */

export function deleteEqpAttrApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备资产档案
 */

export const addEqpAttrApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpAttrApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

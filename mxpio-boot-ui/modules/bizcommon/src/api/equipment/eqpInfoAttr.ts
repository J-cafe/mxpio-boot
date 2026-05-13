import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/attr/list',
  Page = '/erp/equipment/attr/page',
  Delete = '/erp/equipment/attr/remove/',
  Add = '/erp/equipment/attr/add',
  Edit = '/erp/equipment/attr/edit',
}

/**
 * @description: 设备档案列表
 */

export const eqpInfoAttrListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备档案列表
 */

export const eqpInfoAttrPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备档案
 */

export function deleteEqpInfoAttrApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备档案
 */

export const addEqpInfoAttrApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpInfoAttrApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

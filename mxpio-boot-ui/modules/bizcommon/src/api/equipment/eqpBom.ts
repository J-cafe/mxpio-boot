import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/bom/list',
  Page = '/erp/equipment/bom/page',
  Delete = '/erp/equipment/bom/remove/',
  Add = '/erp/equipment/bom/add',
  Edit = '/erp/equipment/bom/edit',
}

/**
 * @description: 设备备件列表
 */

export const eqpBomListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备备件列表
 */

export const eqpBomPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备备件
 */

export function deleteEqpBomApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备备件
 */

export const addEqpBomApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpBomApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

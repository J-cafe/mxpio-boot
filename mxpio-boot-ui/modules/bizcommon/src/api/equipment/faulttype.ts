import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/faulttype/list',
  Page = '/erp/equipment/faulttype/page',
  Delete = '/erp/equipment/faulttype/remove/',
  Add = '/erp/equipment/faulttype/add',
  Edit = '/erp/equipment/faulttype/edit',
}

/**
 * @description: 故障类型列表
 */

export const faulttypeList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 故障类型列表
 */

export const faulttypePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除故障类型
 */

export function deleteFaulttype(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存故障类型
 */

export const addFaulttype = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editFaulttype = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

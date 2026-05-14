import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/eqp/remark/list',
  Page = '/erp/equipment/eqp/remark/page',
  Delete = '/erp/equipment/eqp/remark/delete/',
  Add = '/erp/equipment/eqp/remark/add',
  Edit = '/erp/equipment/eqp/remark/edit',
}

/**
 * @description: 设备资产档案列表
 */

export const eqpRemarkListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备资产档案列表
 */

export const eqpRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备资产档案
 */

export function deleteEqpRemarkApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备资产档案
 */

export const addEqpRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

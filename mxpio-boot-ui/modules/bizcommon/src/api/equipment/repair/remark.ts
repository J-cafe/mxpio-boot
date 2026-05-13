import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotrepair/remark/list',
  Page = '/erp/equipment/spotrepair/remark/page',
  Delete = '/erp/equipment/spotrepair/remark/delete/',
  Add = '/erp/equipment/spotrepair/remark/add',
  Edit = '/erp/equipment/spotrepair/remark/edit',
}

/**
 * @description: 设备资产档案列表
 */

export const eqpRepairRemarkListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备资产档案列表
 */

export const eqpRepairRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备资产档案
 */

export function deleteEqpRepairRemarkApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备资产档案
 */

export const addEqpRepairRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpRepairRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

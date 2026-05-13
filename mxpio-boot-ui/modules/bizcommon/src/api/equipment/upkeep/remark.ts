import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/plan/remark/list',
  Page = '/erp/equipment/upkeep/plan/remark/page',
  Delete = '/erp/equipment/upkeep/plan/remark/delete/',
  Add = '/erp/equipment/upkeep/plan/remark/add',
  Edit = '/erp/equipment/upkeep/plan/remark/edit',
}

/**
 * @description: 设备资产档案列表
 */

export const eqpUpkeepRemarkListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备资产档案列表
 */

export const eqpUpkeepRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备资产档案
 */

export function deleteEqpUpkeepRemarkApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备资产档案
 */

export const addEqpUpkeepRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpUpkeepRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

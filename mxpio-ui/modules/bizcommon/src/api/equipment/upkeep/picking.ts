// 保养领料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/pickupnotice/list',
  Page = '/erp/equipment/upkeep/pickupnotice/page',
  Delete = '/erp/equipment/upkeep/pickupnotice/remove/',
  Reject = '/erp/equipment/upkeep/pickupnotice/reject/',
  Execute = '/erp/equipment/upkeep/pickupnotice/execute/',
  Line = '/erp/equipment/upkeep/pickupnotice/line/list/',
  Save = '/erp/equipment/upkeep/pickupnotice/save',
}

/**
 * @description: 保养领料通知单列表
 */

export const upkeepPickupListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 保养领料通知单分页
 */
export const upkeepPickupPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 保养领料通知单行列表
 */
export const upkeepPickupLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteUpkeepPickupApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + noticeNo,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 拒绝
 */

export const upkeepPickupRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Reject + noticeNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 执行
 */
export const upkeepPickupExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + noticeNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 新增
 */
export const saveUpkeepPickupApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

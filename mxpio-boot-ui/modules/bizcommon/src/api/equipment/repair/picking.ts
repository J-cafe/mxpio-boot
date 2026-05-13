// 保养领料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotrepair/pickupnotice/list',
  Page = '/erp/equipment/spotrepair/pickupnotice/page',
  Delete = '/erp/equipment/spotrepair/pickupnotice/remove/',
  Reject = '/erp/equipment/spotrepair/pickupnotice/reject/',
  Execute = '/erp/equipment/spotrepair/pickupnotice/execute/',
  Line = '/erp/equipment/spotrepair/pickupnotice/line/list/',
  Save = '/erp/equipment/spotrepair/pickupnotice/save',
}

/**
 * @description: 保养领料通知单列表
 */

export const repairPickupListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 保养领料通知单分页
 */
export const repairPickupPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 保养领料通知单行列表
 */
export const repairPickupLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteRepairPickupApi(noticeNo: string) {
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

export const repairPickupRejectApi = (noticeNo: string) => {
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
export const repairPickupExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const saveRepairPickupApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

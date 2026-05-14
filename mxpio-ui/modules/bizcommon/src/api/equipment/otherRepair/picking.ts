// 其他维修领料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/other/repair/pickupnotice/list',
  Page = '/erp/equipment/other/repair/pickupnotice/page',
  Delete = '/erp/equipment/other/repair/pickupnotice/remove/',
  Reject = '/erp/equipment/other/repair/pickupnotice/reject/',
  Execute = '/erp/equipment/other/repair/pickupnotice/execute/',
  Line = '/erp/equipment/other/repair/pickupnotice/line/list/',
  Save = '/erp/equipment/other/repair/pickupnotice/save',
}

/**
 * @description: 其他维修领料通知单列表
 */

export const otherPickupListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 其他维修领料通知单分页
 */
export const otherPickupPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 其他维修领料通知单行列表
 */
export const otherPickupLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteOtherPickupApi(noticeNo: string) {
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

export const otherPickupRejectApi = (noticeNo: string) => {
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
export const otherPickupExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const saveOtherPickupApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

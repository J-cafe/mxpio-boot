// 维修退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotrepair/return/list',
  Page = '/erp/equipment/spotrepair/return/page',
  Delete = '/erp/equipment/spotrepair/return/remove/',
  Reject = '/erp/equipment/spotrepair/return/reject/',
  Execute = '/erp/equipment/spotrepair/return/execute/',
  Line = '/erp/equipment/spotrepair/return/line/list/',
  Save = '/erp/equipment/spotrepair/return/save',
}

/**
 * @description: 维修退料通知单列表
 */

export const repairReturnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 维修退料通知单分页
 */
export const repairReturnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 维修退料通知单行列表
 */
export const repairReturnLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteRepairReturnApi(noticeNo: string) {
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

export const repairReturnRejectApi = (noticeNo: string) => {
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
export const repairReturnExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const saveRepairReturnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

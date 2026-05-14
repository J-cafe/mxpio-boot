// 其他维修退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/other/repair/return/list',
  Page = '/erp/equipment/other/repair/return/page',
  Delete = '/erp/equipment/other/repair/return/remove/',
  Reject = '/erp/equipment/other/repair/return/reject/',
  Execute = '/erp/equipment/other/repair/return/execute/',
  Line = '/erp/equipment/other/repair/return/line/list/',
  Save = '/erp/equipment/other/repair/return/save',
}

/**
 * @description: 其他维修退料通知单列表
 */

export const otherReturnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 其他维修退料通知单分页
 */
export const otherReturnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 其他维修退料通知单行列表
 */
export const otherReturnLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteOtherReturnApi(noticeNo: string) {
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

export const otherReturnRejectApi = (noticeNo: string) => {
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
export const otherReturnExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const saveOtherReturnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

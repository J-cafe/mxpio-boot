// 调拨单出入通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inv/tn/list',
  Page = '/erp/inv/tn/page',
  Delete = '/erp/inv/tn/remove/',
  Reject = '/erp/inv/tn/reject/',
  Execute = '/erp/inv/tn/excute/',
  Isline = '/erp/inv/tnline/list/',
  Save = '/erp/inv/tn/save',
}

/**
 * @description: 调拨单出入通知单列表
 */

export const tnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 调拨单出入通知单分页
 */
export const tnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 调拨单出入通知单行列表
 */
export const tnLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Isline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteTnApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const tnRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const tnExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const saveTnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

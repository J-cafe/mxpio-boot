// 采购验收通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/qo/list',
  Page = '/erp/quality/qo/page',
  Delete = '/erp/quality/qo/remove/',
  Reject = '/erp/quality/qo/reject/',
  Execute = '/erp/quality/qo/submit/',
  Qoline = '/erp/quality/qoline/list/',
  Save = '/erp/quality/qo/save',
}

/**
 * @description: 采购验收通知单列表
 */

export const qoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 采购验收通知单分页
 */
export const qoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 采购验收通知单行列表
 */
export const qoLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + noticeNo });
};

/**
 * @description: 删除
 */

export function qoDeleteApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const qoRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const qoExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const qoSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

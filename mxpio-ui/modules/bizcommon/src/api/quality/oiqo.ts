// 委外验收通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/oiqo/list',
  Page = '/erp/quality/oiqo/page',
  Delete = '/erp/quality/oiqo/remove/',
  Reject = '/erp/quality/oiqo/reject/',
  Execute = '/erp/quality/oiqo/excute/',
  Qoline = '/erp/quality/oiqoline/list/',
  Save = '/erp/quality/oiqo/save',
}

/**
 * @description: 委外验收通知单列表
 */

export const oiqoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 委外验收通知单分页
 */
export const oiqoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 委外验收通知单行列表
 */
export const oiqoLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + noticeNo });
};

/**
 * @description: 删除
 */

export function oiqoDeleteApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const oiqoRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const oiqoExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const oiqoSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

// 生产验收通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/miqo/list',
  Page = '/erp/quality/miqo/page',
  Delete = '/erp/quality/miqo/remove/',
  Reject = '/erp/quality/miqo/reject/',
  Execute = '/erp/quality/miqo/excute/',
  Qoline = '/erp/quality/miqoline/list/',
  Save = '/erp/quality/miqo/save',
}

/**
 * @description: 生产验收通知单列表
 */

export const miqoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 生产验收通知单分页
 */
export const miqoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 生产验收通知单行列表
 */
export const miqoLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + noticeNo });
};

/**
 * @description: 删除
 */

export function miqoDeleteApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const miqoRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const miqoExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const miqoSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

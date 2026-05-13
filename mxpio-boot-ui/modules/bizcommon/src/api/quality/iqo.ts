// 库存质检通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/iqo/list',
  Page = '/erp/quality/iqo/page',
  Delete = '/erp/quality/iqo/remove/',
  Reject = '/erp/quality/iqo/reject/',
  Execute = '/erp/quality/iqo/submit/',
  Qoline = '/erp/quality/iqoline/list/',
  Save = '/erp/quality/iqo/save',
}

/**
 * @description: 库存质检通知单列表
 */

export const iqoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 库存质检通知单分页
 */
export const iqoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 库存质检通知单行列表
 */
export const iqoLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + noticeNo });
};

/**
 * @description: 删除
 */

export function iqoDeleteApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const iqoRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const iqoExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const iqoSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

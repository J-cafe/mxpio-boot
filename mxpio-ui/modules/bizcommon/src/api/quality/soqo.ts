// 销售退货验收通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/soqo/list',
  Page = '/erp/quality/soqo/page',
  Delete = '/erp/quality/soqo/remove/',
  Reject = '/erp/quality/soqo/reject/',
  Execute = '/erp/quality/soqo/submit/',
  Qoline = '/erp/quality/soqoline/list/',
  Save = '/erp/quality/soqo/save',
}

/**
 * @description: 销售退货验收通知单列表
 */

export const soqoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 销售退货验收通知单分页
 */
export const soqoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 销售退货验收通知单行列表
 */
export const soqoLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + noticeNo });
};

/**
 * @description: 删除
 */

export function soqoDeleteApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const soqoRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const soqoExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const soqoSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

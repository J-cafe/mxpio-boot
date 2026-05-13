// 销售发货通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/sales/sn/list',
  Page = '/erp/sales/sn/page',
  Delete = '/erp/sales/sn/remove/',
  Reject = '/erp/sales/sn/reject/',
  Execute = '/erp/sales/sn/execute/',
  Snline = '/erp/sales/snline/list/',
  Save = '/erp/sales/sn/save',
}

/**
 * @description: 销售发货通知单列表
 */

export const saleDeliveryList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 销售发货通知单分页
 */
export const saleDeliveryPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 销售发货通知单行列表
 */
export const snLineList = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Snline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteSaleDelivery(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const saleDeliveryReject = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const saleDeliveryExecute = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const saveSaleDelivery = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

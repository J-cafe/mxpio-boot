// 销售退货通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/sales/rb/list',
  Page = '/erp/sales/rb/page',
  Delete = '/erp/sales/rb/remove/',
  Reject = '/erp/sales/rb/reject/',
  Execute = '/erp/sales/rb/execute/',
  Rbline = '/erp/sales/rbline/list/',
  Save = '/erp/sales/rb/save',
}

/**
 * @description: 销售退货通知单列表
 */

export const saleReturnList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 销售退货通知单分页
 */
export const saleReturnPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 销售退货通知单行列表
 */
export const rbLineList = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Rbline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteSaleReturn(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const saleReturnReject = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const saleReturnExecute = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const saveSaleReturn = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

// 采购退货通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/purc/rj/list',
  Page = '/erp/purc/rj/page',
  Delete = '/erp/purc/rj/remove/',
  Reject = '/erp/purc/rj/reject/',
  Execute = '/erp/purc/rj/execute/',
  Rjline = '/erp/purc/rjline/list/',
  Save = '/erp/purc/rj/save',
}

/**
 * @description: 采购退货通知单列表
 */

export const purchaseReturnList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 采购退货通知单分页
 */
export const purchaseReturnPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 采购退货通知单行列表
 */
export const rjLineList = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Rjline + noticeNo });
};

/**
 * @description: 删除
 */

export function deletePurchaseReturn(noticeNo: string) {
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

export const purchaseReturnReject = (noticeNo: string) => {
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
export const purchaseReturnExecute = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + noticeNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 保存
 */
export const savePurchaseReturn = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

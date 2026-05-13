// 采购到货通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/purc/ro/list',
  Page = '/erp/purc/ro/page',
  Delete = '/erp/purc/ro/remove/',
  Reject = '/erp/purc/ro/reject/',
  Execute = '/erp/purc/ro/execute/',
  Roline = '/erp/purc/roline/list/',
  Save = '/erp/purc/ro/save',
}

/**
 * @description: 采购到货通知单列表
 */

export const purchaseReceiveList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 采购到货通知单分页
 */
export const purchaseReceivePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 采购到货通知单行列表
 */
export const roLineList = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Roline + noticeNo });
};

/**
 * @description: 删除
 */

export function deletePurchaseReceive(noticeNo: string) {
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

export const purchaseReceiveReject = (noticeNo: string) => {
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
export const purchaseReceiveExecute = (noticeNo: string, params: Recordable) => {
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
export const savePurchaseReceive = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

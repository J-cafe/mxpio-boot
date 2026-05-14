import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/purc/po/list',
  Page = '/erp/purc/po/page',
  Delete = '/erp/purc/po/remove/',
  Config = '/erp/common/order/list/PO1',
  Save = '/erp/purc/po/save',
  Poline = '/erp/purc/poline/list/',
  Submit = '/erp/purc/po/submit/',
  Audit = '/erp/purc/po/audit/',
  Abandon = '/erp/purc/po/abandon/',
  Close = '/erp/purc/po/close/',
  Open = '/erp/purc/po/open/',
  Finish = '/erp/purc/po/finish/',
  Clear = '/erp/purc/po/clear/',
  Receive = '/erp/purc/po/execute/receive/',
  Reject = '/erp/purc/po/execute/reject/',
  polinePage = '/erp/purc/poline/page',
  CloseLine = '/erp/purc/poline/close/',
  OpenLine = '/erp/purc/poline/open/',
  SolineUnFinished = '/erp/purc/poline/pageUnFinished',
  ExecutePage = '/erp/purc/poline/vpage',
}

/**
 * @description: 采购订单列表
 */

export const purchaseOrderList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 采购订单分页
 */
export const purchaseOrderPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 采购订单执行报表
 */
export const purchaseExecutePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ExecutePage, params });
};

export const poLineList = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Poline + bizNo });
};

export const poLinePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.polinePage, params });
};

// export const solineUnFinishedList = (params?: RequestCriteriaParams) => {
//   const { http } = useBridge();
//   return http.get({ url: Api.SolineUnFinished, params });
// };

// export const solinePageTwice = (zeroFlag: 1 | 0, params?: RequestCriteriaParams) => {
//   const { http } = useBridge();
//   return http.get({ url: Api.SolinePageTwice + zeroFlag, params });
// };

/**
 * @description: 删除采购订单
 */

export function deletePurchaseOrder(code: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + code,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 采购订单配置
 */

export const purchaseOrderConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增采购订单
 */

export const savePurchaseOrder = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 提交
 */
export const submitPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Submit + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 审核
 */
export const auditPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Audit + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 弃审
 */
export const abandonPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Abandon + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 结算
 */
export const clearPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Clear + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 打开
 */
export const openPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Open + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 关闭
 */
export const closePurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Close + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 完成
 */
export const finishPurchaseOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Finish + bizNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 到货
 */
export const purchaseOrderReceive = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Receive + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 退货
 */
export const purchaseOrderReject = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Reject + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 打开行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const openPoLine = (ids: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.OpenLine + ids },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 关闭行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const closePoLine = (ids: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.CloseLine + ids },
    {
      successMessageMode: 'message',
    },
  );
};

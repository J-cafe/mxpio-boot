import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/sales/so/list',
  Page = '/erp/sales/so/page',
  Delete = '/erp/sales/so/remove/',
  Config = '/erp/common/order/list/SO1',
  Save = '/erp/sales/so/save',
  Soline = '/erp/sales/soline/list/',
  Submit = '/erp/sales/so/submit/',
  Audit = '/erp/sales/so/audit/',
  Abandon = '/erp/sales/so/abandon/',
  Close = '/erp/sales/so/close/',
  Open = '/erp/sales/so/open/',
  Finish = '/erp/sales/so/finish/',
  Clear = '/erp/sales/so/clear/',
  SolinePageTwice = '/erp/sales/soline/page_twice/',
  Execute = '/erp/sales/so/execute/ship/',
  Reject = '/erp/sales/so/execute/reject/',
  CloseLine = '/erp/sales/soline/close/',
  OpenLine = '/erp/sales/soline/open/',
  SolineUnFinished = '/erp/sales/soline/pageUnFinished',
  SolineOfPickage = '/erp/sales/soline/offsettingpickpage',
  SolineOfPage = '/erp/sales/soline/offsettingpage',
}

/**
 * @description: 销售订单列表
 */

export const salesOrderList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const salesOrderPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const soLineList = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Soline + bizNo });
};

export const solineUnFinishedList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SolineUnFinished, params });
};

export const solinePageTwice = (zeroFlag: 1 | 0, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SolinePageTwice + zeroFlag, params });
};

/**
 * @description: 删除销售订单
 */

export function deleteSalesOrder(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 销售订单配置
 */

export const salesOrderConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增销售订单
 */

export const saveSalesOrder = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 结算
 */
export const clearSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Clear + bizNo });
};

/**
 * @description: 打开
 */
export const openSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + bizNo });
};

/**
 * @description: 关闭
 */
export const closeSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + bizNo });
};

/**
 * @description: 完成
 */
export const finishSalesOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish + bizNo });
};

/**
 * @description: 执行
 */
export const salesOrderExecute = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + bizNo, params });
};

/**
 * @description: 退货
 */
export const salesOrderReject = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Reject + bizNo, params });
};

/**
 * @description: 打开行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const openSoLine = (ids: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.OpenLine + ids });
};

/**
 * @description: 关闭行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const closeSoLine = (ids: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.CloseLine + ids });
};

export const solineOfPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SolineOfPage, params });
};

export const solineOfPickage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SolineOfPickage, params });
};

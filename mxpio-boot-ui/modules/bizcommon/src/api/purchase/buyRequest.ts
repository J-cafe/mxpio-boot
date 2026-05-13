import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/purc/br/list',
  Page = '/erp/purc/br/page',
  Delete = '/erp/purc/br/remove/',
  Config = '/erp/common/order/list/BR1',
  Save = '/erp/purc/br/save',
  Brline = '/erp/purc/brline/list/',
  Submit = '/erp/purc/br/submit/',
  Audit = '/erp/purc/br/audit/',
  Reject = '/erp/purc/brline/reject',
  AuditedPage = '/erp/purc/brline/auditedPage',
  BrlineExe = '/erp/purc/brline/exec',
}

/**
 * @description: 请购列表
 */

export const buyRequestList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 请购分页
 */
export const buyRequestPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 请购行明细
 */
export const brLineList = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Brline + bizNo });
};

/**
 * @description: 请购行已审核明细行
 */
export const brLineAuditedPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditedPage, params });
};

/**
 * @description: 删除请购
 */

export function deleteBuyRequest(code: string) {
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
 * @description: 请购配置
 */

export const buyRequestConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增请购
 */

export const saveBuyRequest = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitBuyRequest = (bizNo: string) => {
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
export const auditBuyRequest = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Audit + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 驳回
 */
export const auditBuyReject = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Reject, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 转采购
 */
export const brlineExe = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.BrlineExe, params },
    {
      successMessageMode: 'message',
    },
  );
};

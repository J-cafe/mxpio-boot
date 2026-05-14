import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Page = '/erp/tech/prodrout/page',
  Save = '/erp/tech/prodrout/tmp/save',
  SaveAndSubmit = '/erp/tech/prodrout/tmp/saveAndSubmit',
  Bom = '/erp/tech/prodrout/listByItem/',
  Tmp = '/erp/tech/prodrout/tmp/listByItem/',
  AuditPage = '/erp/tech/prodrout/audit/page',
  Audit = '/erp/tech/prodrout/audit/audit/',
  AuditDetail = '/erp/tech/prodrout/audit/listByItem/',
  AuditLine = '/erp/tech/prodrout/proc/audit/list/',
  ListSubBom = '/erp/tech/bom/listSub',
  History = '/erp/tech/prodrout/history/',
  ProcLine = '/erp/tech/prodrout/proc/list/',
}

// BOM列表
export const procBomPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

// bom历史版本列表
export const procBomHistoryList = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.History + itemCode });
};

// bom版本明细
export const listSubProcBom = (params?: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ListSubBom, params });
};

/**
 * @description: 保存BOM
 */

export const saveProcBom = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

// 保存并提交BOM
export const saveAndSubmitProcBom = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.SaveAndSubmit, params });
};

/**
 * @description: 获取BOM
 */

export function getProcBom(itemCode: string, params: Recordable) {
  const { http } = useBridge();
  return http.post(
    { url: Api.Bom + itemCode, params },
    {
      successMessageMode: 'none',
    },
  );
}

export function procLineList(itemCode: string) {
  const { http } = useBridge();
  return http.get({ url: Api.ProcLine + itemCode });
}

/**
 * @description: 获取BOM草稿
 */

export function getTmpProcBom(itemCode: string) {
  const { http } = useBridge();
  return http.get({ url: Api.Tmp + itemCode });
}

// BOM审核列表
export const procBomAuditPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditPage, params });
};

// BOM审核
export const procBomAudit = (routId: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + routId, params });
};

// BOM审核详情
export const procBomAuditDetail = (routId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditDetail + routId });
};

export const procBomAuditLine = (routId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditLine + routId });
};

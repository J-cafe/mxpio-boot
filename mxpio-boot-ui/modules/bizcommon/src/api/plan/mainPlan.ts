import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/plan/mps/list',
  Page = '/erp/plan/mps/page',
  Delete = '/erp/plan/mps/remove/',
  Save = '/erp/plan/mps/save',
  Change = '/erp/plan/mps/change',
  Mpsline = '/erp/plan/mps/line/list/',
  Submit = '/erp/plan/mps/submit/',
  Audit = '/erp/plan/mps/audit/',
  Abandon = '/erp/plan/mps/abandon/',
  Effect = '/erp/plan/mps/effect/',
  Mrp = '/erp/plan/mps/mrp',
  Po = '/erp/plan/mps/po/page',
  Od = '/erp/plan/mps/od/page',
  Sp = '/erp/plan/mps/sp/page',
  LineSave = '/erp/plan/mps/line/save',
  TempPage = '/erp/plan/mpstemp/page',
  TempAudit = '/erp/plan/mpstemp/audit/',
  TempLineList = '/erp/plan/mpstemp/linetemp/list/',
}

/**
 * @description: 主计划列表
 */

export const mpsListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const mpsPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const mpsTempPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TempPage, params });
};

// @description: 主计划审核草稿行列表
export const mpsTempLineListApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.TempLineList + code });
};

// @description: 主计划审核草稿
export const mpsTempAuditApi = (id: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.TempAudit + id, params });
};

export const mpsPoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Po, params });
};

export const mpsOdPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Od, params });
};

export const mpsSpPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Sp, params });
};

export const mpsLineListApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Mpsline + code });
};

export function mpsDeleteApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增主计划
 */

export const mpsSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 变更主计划
 */
export const mpsChangeApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Change, params });
};

export const mpsLineSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.LineSave, params });
};

/**
 * @description: 提交
 */
export const mpsSubmitApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + code });
};

/**
 * @description: 审核
 */
export const mpsAuditApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + code });
};

/**
 * @description: 弃审
 */
export const mpsAbandonApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + code });
};

/**
 * @description: 生效
 */
export const mpsEffectApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Effect + code });
};

/**
 * @description: 完成
 */
export const mpsMrpApi = () => {
  const { http } = useBridge();
  return http.get({ url: Api.Mrp });
};

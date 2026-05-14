import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/plan/sp/list',
  Page = '/erp/plan/sp/page',
  Delete = '/erp/plan/sp/remove/',
  Save = '/erp/plan/sp/save',
  Line = '/erp/plan/spline/list/',
  Submit = '/erp/plan/sp/submit/',
  Audit = '/erp/plan/sp/audit/',
  Abandon = '/erp/plan/sp/abandon/',
  Close = '/erp/plan/sp/close/',
  Open = '/erp/plan/sp/open/',
  OffsettingPage = '/erp/plan/spline/offsettingpage',
  SpofPage = '/erp/plan/spof/vopage',
  SpofDelete = '/erp/plan/spof/remove/',
  SpofSave = '/erp/plan/spof/save',
  SpofPickage = '/erp/plan/spline/offsettingpickpage',
}

/**
 * @description: 销售预测列表
 */

export const spListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const spPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const spOffsettingPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.OffsettingPage, params });
};

export const spLineApi = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + code });
};

export function spDeleteApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增主计划
 */
export const spSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const spSubmitApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + code });
};

/**
 * @description: 审核
 */
export const spAuditApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + code });
};

/**
 * @description: 弃审
 */
export const spAbandonApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + code });
};

/**
 * @description: 关闭
 */
export const spCloseApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + code });
};

/* @description: 开启
 */
export const spOpenApi = (code: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + code });
};

// @description: 销售预测已冲销列表
export const spofPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SpofPage, params });
};

// @description: 删除销售预测已冲销
export const spofDeleteApi = (code: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.SpofDelete + code,
  });
};

// @description: 保存销售预测冲销
export const spofSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.SpofSave, params });
};

// @description: 销售预测未冲销列表
export const spofPickageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SpofPickage, params });
};

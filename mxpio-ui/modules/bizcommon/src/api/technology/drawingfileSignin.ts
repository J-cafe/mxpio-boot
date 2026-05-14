import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/prodrout/proc/drawingfile/signin/list',
  Page = '/erp/tech/prodrout/proc/drawingfile/signin/page',
  AuditPass = '/erp/tech/prodrout/proc/drawingfile/signin/audit_pass/',
}

/**
 * @description: 工艺图纸签入审核列表
 */

export const drawingfileSigninList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const drawingfileSigninPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 审核通过工艺图纸
 */

export function auditPassDrawingfileSignin(code: string, result: string) {
  const { http } = useBridge();
  return http.get({ url: Api.AuditPass + code + '/' + result });
}

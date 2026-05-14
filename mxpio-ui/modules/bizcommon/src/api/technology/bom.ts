import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Page = '/erp/tech/bom/page',
  Config = '/erp/common/res/list/BomInfo',
  Save = '/erp/tech/bom/tmp/save',
  SaveAndSubmit = '/erp/tech/bom/tmp/saveAndSubmit',
  Bom = '/erp/tech/bom/listByItem/',
  Tmp = '/erp/tech/bom/tmp/listByItem/',
  Consumption = '/erp/tech/bom/consumption',
  AuditPage = '/erp/tech/bom/audit/page',
  Audit = '/erp/tech/bom/audit/audit/',
  AuditDetail = '/erp/tech/bom/audit/listByItem/',
  ExportUnBomList = '/erp/tech/bom/exportUnBomList',
  FindCircularReferenceBoms = '/erp/tech/bom/findCircularReferenceBoms',
  ListSubBom = '/erp/tech/bom/listSub',
  History = '/erp/tech/bom/history/',
  Inversequery = '/erp/tech/bomline/inversequery',
}
// BOM列表
export const bomPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

// bom历史版本列表
export const bomHistoryList = (itemCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.History + itemCode });
};

// bom版本明细
export const listSubBom = (params?: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ListSubBom, params });
};

// BOM列表
export const bomReverseList = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Inversequery, params });
};

/**
 * @description: 配置
 */

export const bomConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存BOM
 */

export const saveBom = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

// 保存并提交BOM
export const saveAndSubmitBom = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.SaveAndSubmit, params });
};

/**
 * @description: 获取BOM
 */

export function getBom(itemCode: string, params: Recordable) {
  const { http } = useBridge();
  return http.post(
    { url: Api.Bom + itemCode, params },
    {
      successMessageMode: 'none',
    },
  );
}

/**
 * @description: 获取BOM草稿
 */

export function getTmpBom(itemCode: string) {
  const { http } = useBridge();
  return http.get({ url: Api.Tmp + itemCode });
}

/**
 * @description: 获取物料材料消耗
 */

export function getItemConsumption(params: Recordable) {
  const { http } = useBridge();
  return http.post({ url: Api.Consumption, params });
}

// BOM审核列表
export const bomAuditpage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditPage, params });
};

// BOM审核
export const bomAudit = (bomId: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bomId, params });
};

// BOM审核详情
export const bomAuditDetail = (bomId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.AuditDetail + bomId });
};

// 导出未配置BOM列表
export function exportUnBomList() {
  const { http } = useBridge();
  return http.post({ url: Api.ExportUnBomList });
}

// 查找有循环引用的BOM
export function findCircularReferenceBoms() {
  const { http } = useBridge();
  return http.get({ url: Api.FindCircularReferenceBoms });
}

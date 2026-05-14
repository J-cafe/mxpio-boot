import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inventory/wo/list',
  Page = '/erp/inventory/wo/page',
  Delete = '/erp/inventory/wo/remove/',
  Config = '/erp/common/res/list/WO1',
  Save = '/erp/inventory/wo/save',
  Woline = '/erp/inventory/woline/list/',
  Submit = '/erp/inventory/wo/submit/',
  Audit = '/erp/inventory/wo/audit/',
  Abandon = '/erp/inventory/wo/abandon/',
  Close = '/erp/inventory/wo/close/',
  Open = '/erp/inventory/wo/open/',
  Execute = '/erp/inventory/wo/excute/',
  Executebatch = '/erp/inventory/wo/excutebatch',
  WolinePage = '/erp/inventory/woline/page',
  IsJit = '/erp/inventory/wo/is_jit_out/',
}

/**
 * @description: 仓单列表
 */

export const woList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const woPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const wolinePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.WolinePage, params });
};

export const wolineList = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Woline + bizNo });
};

/**
 * @description: 删除仓单
 */

export function deleteWo(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 仓单配置
 */

export const woConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存仓单
 */

export const saveWo = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitWo = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditWo = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonWo = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 打开
 */
export const openWo = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + bizNo });
};

/**
 * @description: 关闭
 */
export const closeWo = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + bizNo });
};

/**
 * @description: 执行
 */
export const woExecute = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + bizNo, params });
};

/**
 * @description: 执行
 */
export const woExecutebatchApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Executebatch, params });
};

/**
 * @description: 是否是JIT仓单
 */
export const woIsJitApI = (bizNos: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.IsJit + bizNos });
};

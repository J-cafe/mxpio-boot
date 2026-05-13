import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inventory/to/list',
  Page = '/erp/inventory/to/page',
  Delete = '/erp/inventory/to/remove/',
  Config = '/erp/common/res/list/WO1',
  Save = '/erp/inventory/to/save',
  Toline = '/erp/inventory/toline/list/',
  Submit = '/erp/inventory/to/submit/',
  Audit = '/erp/inventory/to/audit/',
  Abandon = '/erp/inventory/to/abandon/',
  Close = '/erp/inventory/to/close/',
  Open = '/erp/inventory/to/open/',
  Execute = '/erp/inventory/to/excute/',
  TolinePage = '/erp/inventory/toline/page',
}

/**
 * @description: 调拨列表
 */

export const toListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const toPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const tolinePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TolinePage, params });
};

export const tolineListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Toline + bizNo });
};

/**
 * @description: 删除调拨
 */

export function deleteToApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 调拨配置
 */

export const toConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存调拨
 */

export const saveToApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitToApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditToApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonToApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 打开
 */
export const openToApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + bizNo });
};

/**
 * @description: 关闭
 */
export const closeToApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + bizNo });
};

/**
 * @description: 执行
 */
export const toExecuteApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + bizNo, params });
};

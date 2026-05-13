import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/ii/list',
  Page = '/erp/quality/ii/page',
  Delete = '/erp/quality/ii/remove/',
  Config = '/erp/common/res/list/InspectionItem',
  Save = '/erp/quality/ii/save',
  Submit = '/erp/quality/ii/submit/',
  Audit = '/erp/quality/ii/audit/',
  Abandon = '/erp/quality/ii/abandon/',
  LineList = '/erp/quality/ii/param/list/',
}

/**
 * @description: 检测项列表
 */

export const iiListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const iiPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const paramlineListApi = (code) => {
  const { http } = useBridge();
  return http.get({ url: Api.LineList + code });
};

/**
 * @description: 删除检测项
 */

export function deleteIiApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 检测项配置
 */

export const iiConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增检测项
 */

export const saveIiApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitIiApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditIiApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonIiApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

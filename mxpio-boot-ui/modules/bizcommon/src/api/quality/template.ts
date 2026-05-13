import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/is/list',
  Page = '/erp/quality/is/page',
  Delete = '/erp/quality/is/remove/',
  Config = '/erp/common/res/list/InspectionScheme',
  Save = '/erp/quality/is/save',
  Submit = '/erp/quality/is/submit/',
  Audit = '/erp/quality/is/audit/',
  Abandon = '/erp/quality/is/abandon/',
  LineList = '/erp/quality/isline/list/',
}

/**
 * @description: 检测方案列表
 */
export const qtListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const qtPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const qtLineListApi = (code) => {
  const { http } = useBridge();
  return http.get({ url: Api.LineList + code });
};

/**
 * @description: 删除检测方案
 */
export function deleteQtApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 检测方案配置
 */
export const qtConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增检测方案
 */
export const saveQtApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const submitQtApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditQtApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonQtApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

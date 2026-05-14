import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Page = '/erp/plan/pm/page',
  Convert = '/erp/plan/pm/convert',
  Entrust = '/erp/plan/pm/entrust',
  Save = '/erp/plan/pm/save',
  Split = '/erp/plan/pm/split/',
  ConvertOutsource = '/erp/plan/pm/convertoo',
}

export const pmPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 保存制造计划
 */

export const pmSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 拆分计划
 */
export const pmSplitApi = (planNo: string, splitQuantity: number) => {
  const { http } = useBridge();
  return http.get({ url: Api.Split + planNo + '/' + splitQuantity });
};

/**
 * @description: 转订单
 */
export const pmConvertApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Convert, params });
};

/**
 * @description: 转委外
 */
export const pmEntrustApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Entrust, params });
};

/**
 * @description: 转委外订单
 */

export const pmConvertOutsourceApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ConvertOutsource, params });
};

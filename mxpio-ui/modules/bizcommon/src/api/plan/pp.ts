import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Page = '/erp/plan/pp/page',
  Convert = '/erp/plan/pp/convert',
  Save = '/erp/plan/pp/save',
  Split = '/erp/plan/pp/split/',
  ConvertOutsource = '/erp/plan/pp/convertoo',
}

export const ppPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 保存制造计划
 */

export const ppSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 拆分计划
 */
export const ppSplitApi = (planNo: string, splitQuantity: number) => {
  const { http } = useBridge();
  return http.get({ url: Api.Split + planNo + '/' + splitQuantity });
};

/**
 * @description: 转订单
 */
export const ppConvertApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Convert, params });
};

/**
 * @description: 转委外订单
 */

export const ppConvertOutsourceApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ConvertOutsource, params });
};

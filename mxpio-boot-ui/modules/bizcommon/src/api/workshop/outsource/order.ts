import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/oo/list',
  Page = '/erp/workshop/oo/page',
  Delete = '/erp/workshop/oo/remove/',
  Config = '/erp/common/order/list/PU1',
  Save = '/erp/workshop/oo/save',
  Ooline = '/erp/workshop/ooline/list/',
  OolineList = '/erp/workshop/ooline/list',
  Submit = '/erp/workshop/oo/submit/',
  Audit = '/erp/workshop/oo/audit/',
  Abandon = '/erp/workshop/oo/abandon/',
  Close = '/erp/workshop/oo/close/',
  Open = '/erp/workshop/oo/open/',
  Finish = '/erp/workshop/oo/finish/',
  Clear = '/erp/workshop/oo/clear/',
  Release = '/erp/workshop/oo/order/',
  SaveBom = '/erp/workshop/ooline/batch/save',
  Execute = '/erp/workshop/oo/excute',
  ExcuteScrap = '/erp/workshop/oo/excuteScrap/',
}

/**
 * @description: 委外订单列表
 */

export const outsourceOrderListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const outsourceOrderPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 委外订单行列表
 */
export const ooLineListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Ooline + bizNo });
};

/**
 * @description: 委外订单行列表
 */
export const outsourceLineListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.OolineList, params });
};

/**
 * @description: 删除委外订单
 */

export function deleteOutsourceOrderApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 委外订单配置
 */

export const outsourceOrderConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增委外订单
 */

export const saveOutsourceOrderApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 保存委外订单BOM
 */

export const saveOutsourceBomApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.SaveBom, params });
};

/**
 * @description: 提交
 */
export const submitOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 打开
 */
export const openOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + bizNo });
};

/**
 * @description: 关闭
 */
export const closeOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + bizNo });
};

/**
 * @description: 完成
 */
export const finishOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish + bizNo });
};

/**
 * @description: 结算
 */
export const clearOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Clear + bizNo });
};

/**
 * @description: 下达
 */
export const releaseOutsourceOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Release + bizNo });
};

/**
 * @description: 委外订单领退料
 */
export const outsourcePickingApi = (type: string, bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + '/' + type + '/' + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 委外订单报工
 */
export const outsourceExecuteApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + '/' + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 委外订单不合格退料
 */
export const executeScrapOutsourceApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.ExcuteScrap + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

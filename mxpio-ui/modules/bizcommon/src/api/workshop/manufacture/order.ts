import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/mo/list',
  Page = '/erp/workshop/mo/page',
  Delete = '/erp/workshop/mo/remove/',
  Config = '/erp/common/order/list/MO1',
  Save = '/erp/workshop/mo/save',
  Moline = '/erp/workshop/moline/list/',
  MolineList = '/erp/workshop/moline/list',
  Submit = '/erp/workshop/mo/submit/',
  Audit = '/erp/workshop/mo/audit/',
  Abandon = '/erp/workshop/mo/abandon/',
  Close = '/erp/workshop/mo/close/',
  Open = '/erp/workshop/mo/open/',
  Finish = '/erp/workshop/mo/finish/',
  Clear = '/erp/workshop/mo/clear/',
  Release = '/erp/workshop/mo/order/',
  SaveBom = '/erp/workshop/moline/batch/save',
  Materialkitting = '/erp/workshop/mo/materialkitting',
  Execute = '/erp/workshop/mo/excute',
  ExcuteScrap = '/erp/workshop/mo/excuteScrap/',
  Computeset = '/erp/workshop/moline/computeset/',
}

/**
 * @description: 生产订单列表
 */

export const manufactureOrderList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const manufactureOrderPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 生产订单行列表
 */
export const moLineList = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Moline + bizNo });
};

/**
 * @description: 整套领料行列表
 */
export const computesetListApi = (bizNo: string, quantity) => {
  const { http } = useBridge();
  return http.get({ url: Api.Computeset + bizNo + '/' + quantity });
};

/**
 * @description: 生产订单行列表
 */
export const manufactureLineListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.MolineList, params });
};

/**
 * @description: 删除生产订单
 */

export function deleteManufactureOrder(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 生产订单配置
 */

export const manufactureOrderConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增生产订单
 */

export const saveManufactureOrder = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 保存生产订单BOM
 */

export const saveManufactureBom = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.SaveBom, params });
};

/**
 * @description: 提交
 */
export const submitManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const auditManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const abandonManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 打开
 */
export const openManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Open + bizNo });
};

/**
 * @description: 关闭
 */
export const closeManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close + bizNo });
};

/**
 * @description: 完成
 */
export const finishManufactureOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish + bizNo });
};

/**
 * @description: 结算
 */
export const clearManufactureOrderApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Clear + bizNo });
};

/**
 * @description: 下达
 */
export const releaseManufactureOrder = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Release + bizNo });
};

/**
 * @description: 齐套分析
 */
export const materialkittingApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Materialkitting, params });
};

/**
 * @description: 生产订单领退料
 */
export const manufactureMoPickingApi = (type: string, bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + '/' + type + '/' + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 生产订单报工
 */
export const manufactureExecuteApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + '/' + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 生产订单不合格退料
 */
export const executeScrapManufactureApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.ExcuteScrap + bizNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

// 库存质检申请单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/iia/list',
  Page = '/erp/quality/iia/page',
  Delete = '/erp/quality/iia/remove/',
  Qoline = '/erp/quality/iialine/list/',
  Save = '/erp/quality/iia/save',
  Submit = '/erp/quality/iia/submit/',
  Audit = '/erp/quality/iia/agree/',
  Abandon = '/erp/quality/iia/abandon/',
  Config = '/erp/common/order/list/IIA1',
}

/**
 * @description: 库存质检申请单列表
 */

export const iiaListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 库存质检申请单分页
 */
export const iiaPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 库存质检申请单行列表
 */
export const iiaLineListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + bizNo });
};

/**
 * @description: 库存质检申请单配置
 */

export const iiaConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除
 */

export function iiaDeleteApi(bizNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + bizNo,
  });
}

/**
 * @description: 保存
 */
export const iiaSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交审核
 */
export const iiaSubmitApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const iiaAuditApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const iiaAbandonApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

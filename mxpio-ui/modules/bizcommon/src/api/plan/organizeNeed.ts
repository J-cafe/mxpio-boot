import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/plan/od/list',
  Page = '/erp/plan/od/page',
  Delete = '/erp/plan/od/remove/',
  Config = '/erp/common/order/list/OD1',
  Save = '/erp/plan/od/save',
  Soline = '/erp/plan/odline/list/',
  Submit = '/erp/plan/od/submit/',
  Audit = '/erp/plan/od/audit/',
  Abandon = '/erp/plan/od/abandon/',
  Finish = '/erp/plan/od/finish/',
  Clear = '/erp/plan/od/clear/',
  OdlinePageTwice = '/erp/plan/odline/page_twice/',
  CloseLine = '/erp/plan/odline/close/',
  OpenLine = '/erp/plan/odline/open/',
  Receive = '/erp/plan/od/execute/receive/',
  Reject = '/erp/plan/od/execute/reject/',
  Refill = '/erp/plan/od/execute/refill/',
  Scrap = '/erp/plan/od/defect/excute_scrap/',
}

/**
 * @description: 组织需求列表
 */

export const odListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const odPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const odLineListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Soline + bizNo });
};

export const odlinePageTwiceApi = (zeroFlag: 1 | 0, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.OdlinePageTwice + zeroFlag, params });
};

/**
 * @description: 删除组织需求
 */

export function odDeleteApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 组织需求配置
 */

export const odConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增组织需求
 */

export const odSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交
 */
export const odSubmitApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

/**
 * @description: 审核
 */
export const odAuditApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Audit + bizNo });
};

/**
 * @description: 弃审
 */
export const odAbandonApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Abandon + bizNo });
};

/**
 * @description: 结算
 */
export const odClearApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Clear + bizNo });
};

/**
 * @description: 完成
 */
export const odFinishApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish + bizNo });
};

/**
 * @description: 打开行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const openOdLineApi = (ids: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.OpenLine + ids });
};

/**
 * @description: 关闭行
 * @param ids bizNo:lineNo拼接的字符串，多个用逗号分隔
 */
export const closeOdLineApi = (ids: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.CloseLine + ids });
};

/**
 * @description: 领料
 */
export const odReceiveApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Receive + bizNo, params });
};

/**
 * @description: 退料
 */
export const odRejectApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + bizNo, params });
};

/**
 * @description: 补料
 */
export const odRefillApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Refill + bizNo, params });
};

/**
 * @description: 不合格退料
 */
export const odScrapApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Scrap + bizNo, params });
};

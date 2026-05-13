import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Flow = '/erp/inventory/flow/page',
  FlowSum = '/erp/inventory/flow/sum/page',
  Lot = '/erp/inventory/flow/lot/sum/page',
  LotExecuted = '/erp/inventory/lot/executed/',
}

/**
 * @description: 即时库存列表
 */

export const flowSumPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.FlowSum, params });
};

/**
 * @description: 库存流水列表
 */
export const flowPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Flow, params });
};

/**
 * @description: 批次库存列表
 */
export const lotPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Lot, params });
};

/**
 * @description: 业务单已执行批次列表
 */
export const lotExecutedPage = (
  bizNo: string,
  lineNo: string,
  accessType: string,
  params?: RequestCriteriaParams,
) => {
  const { http } = useBridge();
  return http.get({ url: Api.LotExecuted + bizNo + '/' + lineNo + '/' + accessType, params });
};

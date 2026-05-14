// 初始化入库通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inv/initstock/list',
  Page = '/erp/inv/initstock/page',
  Delete = '/erp/inv/initstock/remove/',
  Reject = '/erp/inv/initstock/reject/',
  Execute = '/erp/inv/initstock/excute/',
  Isline = '/erp/inv/initstockline/list/',
  Save = '/erp/inv/initstock/save',
}

/**
 * @description: 初始化入库通知单列表
 */

export const initstockListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 初始化入库通知单分页
 */
export const initstockPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 初始化入库通知单行列表
 */
export const isLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Isline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteInitstockApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + noticeNo,
  });
}

/**
 * @description: 拒绝
 */

export const initstockRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Reject + noticeNo });
};

/**
 * @description: 执行
 */
export const initstockExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Execute + noticeNo, params });
};

/**
 * @description: 保存
 */
export const saveInitstockApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

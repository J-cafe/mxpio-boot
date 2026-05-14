// 委外入库通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/oomn/list',
  Page = '/erp/workshop/oomn/page',
  Delete = '/erp/workshop/oomn/remove/',
  Reject = '/erp/workshop/oomn/reject/',
  Execute = '/erp/workshop/oomn/excute/',
  Mpline = '/erp/workshop/oomnline/list/',
  Add = '/erp/workshop/oomnline/add',
  Edit = '/erp/workshop/oomnline/edit',
}

/**
 * @description: 委外入库通知单列表
 */

export const oomnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 委外入库通知单分页
 */
export const oomnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 委外入库通知单行列表
 */
export const oomnLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Mpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteOomnnApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + noticeNo,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 拒绝
 */

export const oomnRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Reject + noticeNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 执行
 */
export const oomnExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + noticeNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 新增
 */
export const addOomnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Add, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 编辑
 */
export const editOomnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

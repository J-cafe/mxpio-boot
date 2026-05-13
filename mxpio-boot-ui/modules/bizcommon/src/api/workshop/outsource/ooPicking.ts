// 委外领退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/op/list',
  Page = '/erp/workshop/op/page',
  Delete = '/erp/workshop/op/remove/',
  Reject = '/erp/workshop/op/reject/',
  Execute = '/erp/workshop/op/excute/',
  Mpline = '/erp/workshop/opline/list/',
  Add = '/erp/workshop/opline/add',
  Edit = '/erp/workshop/opline/edit',
}

/**
 * @description: 委外领退补料通知单列表
 */

export const opListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 委外领退补料通知单分页
 */
export const opPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 委外领退补料通知单行列表
 */
export const opLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Mpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteOpApi(noticeNo: string) {
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

export const opRejectApi = (noticeNo: string) => {
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
export const opExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const addOpApi = (params: Recordable) => {
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
export const editOpApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

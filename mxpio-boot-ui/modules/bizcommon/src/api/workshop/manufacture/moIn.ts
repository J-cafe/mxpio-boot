// 生产入库通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/mn/list',
  Page = '/erp/workshop/mn/page',
  Delete = '/erp/workshop/mn/remove/',
  Reject = '/erp/workshop/mn/reject/',
  Execute = '/erp/workshop/mn/excute/',
  Mpline = '/erp/workshop/mnline/list/',
  Add = '/erp/workshop/mnline/add',
  Edit = '/erp/workshop/mnline/edit',
}

/**
 * @description: 生产入库通知单列表
 */

export const mnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 生产入库通知单分页
 */
export const mnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 生产入库通知单行列表
 */
export const mnLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Mpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteMnApi(noticeNo: string) {
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

export const mnRejectApi = (noticeNo: string) => {
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
export const mnExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const addMnApi = (params: Recordable) => {
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
export const editMnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

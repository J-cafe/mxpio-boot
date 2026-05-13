// 生产领退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/mp/list',
  Page = '/erp/workshop/mp/page',
  Delete = '/erp/workshop/mp/remove/',
  Reject = '/erp/workshop/mp/reject/',
  Execute = '/erp/workshop/mp/excute/',
  Mpline = '/erp/workshop/mpline/list/',
  Add = '/erp/workshop/mpline/add',
  Edit = '/erp/workshop/mpline/edit',
}

/**
 * @description: 生产领退补料通知单列表
 */

export const mpListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 生产领退补料通知单分页
 */
export const mpPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 生产领退补料通知单行列表
 */
export const mpLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Mpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteMpApi(noticeNo: string) {
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

export const mpRejectApi = (noticeNo: string) => {
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
export const mpExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const addMpApi = (params: Recordable) => {
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
export const editMpApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

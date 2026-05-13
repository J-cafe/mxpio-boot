// 委外不合格退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/oodp/list',
  Page = '/erp/workshop/oodp/page',
  Delete = '/erp/workshop/oodp/remove/',
  Reject = '/erp/workshop/oodp/reject/',
  Execute = '/erp/workshop/oodp/excute/',
  Dpline = '/erp/workshop/oodpline/list/',
  Add = '/erp/workshop/oodpline/add',
  Edit = '/erp/workshop/oodpline/edit',
  ExceptionType = '/erp/workshop/oodpline/exception_tpye_list/',
}

/**
 * @description: 委外不合格退料通知单列表
 */

export const oodpListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 委外不合格退料通知单分页
 */
export const oodpPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 委外不合格退料通知单行列表
 */
export const oodpLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Dpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteOodpApi(noticeNo: string) {
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

export const oodpRejectApi = (noticeNo: string) => {
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
export const oodpExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const addOodpApi = (params: Recordable) => {
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
export const editOodpApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

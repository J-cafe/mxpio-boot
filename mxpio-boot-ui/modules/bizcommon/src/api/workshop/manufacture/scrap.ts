// 生产不合格退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/workshop/dp/list',
  Page = '/erp/workshop/dp/page',
  Delete = '/erp/workshop/dp/remove/',
  Reject = '/erp/workshop/dp/reject/',
  Execute = '/erp/workshop/dp/excute/',
  Dpline = '/erp/workshop/dpline/list/',
  Add = '/erp/workshop/dpline/add',
  Edit = '/erp/workshop/dpline/edit',
  ExceptionType = '/erp/workshop/dpline/exception_tpye_list/',
}

/**
 * @description: 生产不合格退料通知单列表
 */

export const manufactureScrapListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 生产不合格退料通知单分页
 */
export const manufactureScrapPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 生产不合格退料通知单行列表
 */
export const dpLineListApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Dpline + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteManufactureScrapApi(noticeNo: string) {
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

export const manufactureScrapRejectApi = (noticeNo: string) => {
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
export const manufactureScrapExecuteApi = (noticeNo: string, params: Recordable) => {
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
export const addManufactureScrapApi = (params: Recordable) => {
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
export const editManufactureScrapApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 不合格原因列表
 */
export const exceptionTypeListApi = (noticeNo: string, lineNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ExceptionType + noticeNo + '/' + lineNo });
};

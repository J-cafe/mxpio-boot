import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/common/notice/list',
  Page = '/erp/common/notice/page',
  Delete = '/erp/common/notice/remove/',
  Add = '/erp/common/notice/add',
  Edit = '/erp/common/notice/edit',
  Config = '/erp/common/notice/list/',
}

/**
 * @description: 列表
 */

export const noticeList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 列表
 */

export const noticePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除
 */

export function deleteNotice(code: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + code,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 新增
 */

export const addNotice = (params: Recordable) => {
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

export const editNotice = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 获取配置
 */
export const noticeConfig = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config + code });
};

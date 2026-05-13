import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/common/res/list',
  Page = '/erp/common/res/page',
  Delete = '/erp/common/res/remove/',
  Add = '/erp/common/res/add',
  Edit = '/erp/common/res/edit',
  Config = '/erp/common/res/list/',
}

/**
 * @description: 列表
 */

export const resList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 列表
 */

export const resPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除
 */

export function deleteRes(code: string) {
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

export const addRes = (params: Recordable) => {
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

export const editRes = (params: Recordable) => {
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
export const resConfig = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config + code });
};

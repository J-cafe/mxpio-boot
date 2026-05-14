import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/workunit/list',
  Page = '/erp/tech/workunit/page',
  Delete = '/erp/tech/workunit/remove/',
  Config = '/erp/common/res/list/WorkUnit',
  Add = '/erp/tech/workunit/add',
  Edit = '/erp/tech/workunit/edit',
}

/**
 * @description: 作业单元列表
 */

export const workunitList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 作业单元列表
 */

export const workunitPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 作业单元配置
 */

export const workunitConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除作业单元
 */

export function deleteWorkunit(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增作业单元
 */

export const addWorkunit = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑作业单元
 */

export function editWorkunit(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

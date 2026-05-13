import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/workcenter/list',
  Page = '/erp/tech/workcenter/page',
  Delete = '/erp/tech/workcenter/remove/',
  Config = '/erp/common/res/list/WorkCenter',
  Add = '/erp/tech/workcenter/add',
  Edit = '/erp/tech/workcenter/edit',
}

/**
 * @description: 工作中心列表
 */

export const workCenterList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const workCenterPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 工作中心配置
 */

export const workCenterConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除工作中心
 */

export function deleteWorkCenter(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增工作中心
 */

export const addWorkCenter = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑工作中心
 */

export function editWorkCenter(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

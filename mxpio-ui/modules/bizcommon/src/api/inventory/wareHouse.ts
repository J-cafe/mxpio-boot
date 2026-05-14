// 仓库列表
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/inventory/wh/list',
  Page = '/erp/inventory/wh/page',
  Delete = '/erp/inventory/wh/remove/',
  Config = '/erp/common/res/list/Warehouse',
  Add = '/erp/inventory/wh/add',
  Edit = '/erp/inventory/wh/edit',
}

/**
 * @description: 仓库列表
 */

export const whList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const whPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除仓库
 */

export function deleteWh(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 仓库配置
 */

export const whConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增仓库
 */

export const addWh = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑仓库
 */

export function editWh(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

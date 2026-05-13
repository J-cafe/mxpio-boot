import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/factory/list',
  Page = '/erp/tech/factory/page',
  Delete = '/erp/tech/factory/remove/',
  Config = '/erp/common/res/list/Factory',
  Add = '/erp/tech/factory/add',
  Edit = '/erp/tech/factory/edit',
}

/**
 * @description: 工厂列表
 */

export const factoryList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const factoryPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除工厂
 */

export function deleteFactory(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 工厂配置
 */

export const factoryConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增工厂
 */

export const addFactory = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑工厂
 */

export function editFactory(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

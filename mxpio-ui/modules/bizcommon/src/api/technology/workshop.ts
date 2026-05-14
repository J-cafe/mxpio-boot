import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/workshop/list',
  Page = '/erp/tech/workshop/page',
  Delete = '/erp/tech/workshop/remove/',
  Config = '/erp/common/res/list/WorkShop',
  Add = '/erp/tech/workshop/add',
  Edit = '/erp/tech/workshop/edit',
}

/**
 * @description: 车间列表
 */

export const workshopList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 车间列表
 */

export const workshopPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 车间配置
 */

export const workshopConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除车间
 */

export function deleteWorkshop(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增车间
 */

export const addWorkshop = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑车间
 */

export function editWorkshop(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

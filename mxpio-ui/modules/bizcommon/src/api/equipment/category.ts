import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/category/list',
  Page = '/erp/equipment/category/page',
  Tree = '/erp/equipment/category/tree',
  Delete = '/erp/equipment/category/remove/',
  Add = '/erp/equipment/category/add',
  Edit = '/erp/equipment/category/edit',
  ChildList = '/erp/equipment/category/childList/',
}

/**
 * @description: 设备分类列表
 */

export const eqpCategoryListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 故障类型列表
 */

export const eqpCategoryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const eqpCategoryTreeApi = () => {
  const { http } = useBridge();
  return http.get({ url: Api.Tree });
};

export const eqpCategoryChildListApi = (pid: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ChildList + pid });
};

/**
 * @description: 删除故障类型
 */

export function deleteEqpCategoryApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存故障类型
 */

export const addEqpCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

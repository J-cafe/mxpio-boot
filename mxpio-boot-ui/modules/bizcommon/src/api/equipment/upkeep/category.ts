import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/category/list',
  Page = '/erp/equipment/upkeep/category/page',
  Delete = '/erp/equipment/upkeep/category/remove/',
  Add = '/erp/equipment/upkeep/category/add',
  Edit = '/erp/equipment/upkeep/category/edit',
  DetailPage = '/erp/equipment/upkeep/category/detail/page',
  DetailDelete = '/erp/equipment/upkeep/category/detail/remove/',
  DetailAdd = '/erp/equipment/upkeep/category/detail/add',
  DetailEdit = '/erp/equipment/upkeep/category/detail/edit',
  DetailParamPage = '/erp/equipment/upkeep/category/param/page',
}

/**
 * @description: 设备保养分类列表
 */
export const eqpUpkeepCategoryListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备保养分类列表
 */
export const eqpUpkeepCategoryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备保养分类
 */

export function deleteEqpUpkeepCategoryApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备保养分类
 */

export const addEqpUpkeepCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpUpkeepCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 保养明细列表
export const eqpUpkeepCategoryDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 删除保养明细
export const deleteEqpUpkeepCategoryDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备保养明细
 */
export const addEqpUpkeepCategoryDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑保养明细
export const editEqpUpkeepCategoryDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 方案明细选项列表
export const eqpUpkeepCategoryParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamPage, params });
};

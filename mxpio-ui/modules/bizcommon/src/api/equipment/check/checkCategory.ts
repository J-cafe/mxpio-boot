import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotcheck/tpmCheckCategory/list',
  Page = '/erp/equipment/spotcheck/tpmCheckCategory/page',
  Delete = '/erp/equipment/spotcheck/tpmCheckCategory/delete/',
  Add = '/erp/equipment/spotcheck/tpmCheckCategory/add',
  Edit = '/erp/equipment/spotcheck/tpmCheckCategory/edit',
  DetailPage = '/erp/equipment/spotcheck/tpmCheckDetail/page',
  DetailDelete = '/erp/equipment/spotcheck/tpmCheckDetail/delete/',
  DetailAdd = '/erp/equipment/spotcheck/tpmCheckDetail/add',
  DetailEdit = '/erp/equipment/spotcheck/tpmCheckDetail/edit',
  DetailParamPage = '/erp/equipment/spotcheck/tpmCheckDetailParam/page',
}

/**
 * @description: 设备点检分类列表
 */
export const eqpCheckCategoryListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备点检分类列表
 */
export const eqpCheckCategoryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备点检分类
 */

export function deleteEqpCheckCategoryApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备点检分类
 */

export const addEqpCheckCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpCheckCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 点检明细列表
export const eqpCheckCategoryDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 删除点检明细
export const deleteEqpCheckCategoryDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备点检明细
 */
export const addEqpCheckCategoryDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑点检明细
export const editEqpCheckCategoryDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 方案明细选项列表
export const eqpCheckCategoryDetailParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamPage, params });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/plan/list',
  Page = '/erp/equipment/upkeep/plan/page',
  Delete = '/erp/equipment/upkeep/plan/remove/',
  Add = '/erp/equipment/upkeep/plan/add',
  Edit = '/erp/equipment/upkeep/plan/edit',
  DetailPage = '/erp/equipment/upkeep/plan/detail/page',
  DetailDelete = '/erp/equipment/upkeep/plan/detail/remove/',
  DetailAdd = '/erp/equipment/upkeep/plan/detail/add',
  DetailEdit = '/erp/equipment/upkeep/plan/detail/edit',
  TargetPage = '/erp/equipment/upkeep/plan/target/page',
  TargetDelete = '/erp/equipment/upkeep/plan/target/remove/',
  TargetAdd = '/erp/equipment/upkeep/plan/target/add',
  TargetEdit = '/erp/equipment/upkeep/plan/target/edit',
  TargetAddByCategory = '/erp/equipment/upkeep/plan/target/addByCategory',
  DetailAddByCategory = '/erp/equipment/upkeep/plan/detail/addByCategory',
  Enable = '/erp/equipment/upkeep/plan/enable/',
  Recover = '/erp/equipment/upkeep/plan/recover/',
  Disable = '/erp/equipment/upkeep/plan/disable/',
  Pause = '/erp/equipment/upkeep/plan/pause/',
  Copy = '/erp/equipment/upkeep/plan/copy/',
  ParamPage = '/erp/equipment/upkeep/plan/detail/param/page',
}

/**
 * @description: 设备保养分类列表
 */
export const eqpUpkeepPlanListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备保养分类列表
 */
export const eqpUpkeepPlanPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备保养分类
 */

export function deleteEqpUpkeepPlanApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备保养分类
 */

export const addEqpUpkeepPlanApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpUpkeepPlanApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 保养明细计划列表
export const eqpUpkeepPlanDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 删除保养计划明细
export const deleteEqpUpkeepPlanDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备保养计划明细
 */
export const addEqpUpkeepPlanDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑保养计划明细
export const editEqpUpkeepPlanDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 保养目标计划列表
export const eqpUpkeepPlanTargetPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TargetPage, params });
};

// 删除保养目标计划
export const deleteEqpUpkeepPlanTargetApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.TargetDelete + id,
  });
};

// 保存保养目标计划
export const addEqpUpkeepPlanTargetApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.TargetAdd, params });
};

// 编辑保养目标计划
export const editEqpUpkeepPlanTargetApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.TargetEdit, params });
};

// 按类别新增保养目标计划
export const addEqpUpkeepPlanTargetByCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.TargetAddByCategory, params });
};

// 按类别新增保养计划明细
export const addEqpUpkeepPlanDetailByCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAddByCategory, params });
};

// 启用保养计划
export const enableEqpUpkeepPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Enable + planId });
};

// 作废保养计划
export const disableEqpUpkeepPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Disable + planId });
};

// 停止保养计划
export const pauseEqpUpkeepPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Pause + planId });
};

// 恢复保养计划
export const recoverEqpUpkeepPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Recover + planId });
};

// 复制保养计划
export const copyEqpUpkeepPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Copy + planId });
};

// 保养计划明细选项列表
export const eqpUpkeepPlanParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ParamPage, params });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotcheck/tpmCheckPlan/list',
  Page = '/erp/equipment/spotcheck/tpmCheckPlan/page',
  Delete = '/erp/equipment/spotcheck/tpmCheckPlan/delete/',
  Add = '/erp/equipment/spotcheck/tpmCheckPlan/add',
  Edit = '/erp/equipment/spotcheck/tpmCheckPlan/edit',
  DetailPage = '/erp/equipment/spotcheck/tpmCheckPlanDetail/page',
  DetailDelete = '/erp/equipment/spotcheck/tpmCheckPlanDetail/delete/',
  DetailAdd = '/erp/equipment/spotcheck/tpmCheckPlanDetail/add',
  DetailEdit = '/erp/equipment/spotcheck/tpmCheckPlanDetail/edit',
  TargetPage = '/erp/equipment/spotcheck/tpmCheckPlanTarget/page',
  TargetDelete = '/erp/equipment/spotcheck/tpmCheckPlanTarget/delete/',
  TargetAdd = '/erp/equipment/spotcheck/tpmCheckPlanTarget/add',
  TargetEdit = '/erp/equipment/spotcheck/tpmCheckPlanTarget/edit',
  TargetAddByCategory = '/erp/equipment/spotcheck/tpmCheckPlanTarget/addByCategory',
  DetailAddByCategory = '/erp/equipment/spotcheck/tpmCheckPlanDetail/add/',
  Enable = '/erp/equipment/spotcheck/tpmCheckPlan/enable/',
  Recover = '/erp/equipment/spotcheck/tpmCheckPlan/recover/',
  Disable = '/erp/equipment/spotcheck/tpmCheckPlan/disable/',
  Pause = '/erp/equipment/spotcheck/tpmCheckPlan/pause/',
  Copy = '/erp/equipment/spotcheck/tpmCheckPlan/copy/',
  DetailParamPage = '/erp/equipment/spotcheck/tpmCheckPlanDetailParam/page',
}

/**
 * @description: 设备点检分类列表
 */
export const eqpCheckPlanListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备点检分类列表
 */
export const eqpCheckPlanPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备点检分类
 */

export function deleteEqpCheckPlanApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备点检分类
 */

export const addEqpCheckPlanApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpCheckPlanApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 点检明细计划列表
export const eqpCheckPlanDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 删除点检计划明细
export const deleteEqpCheckPlanDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备点检计划明细
 */
export const addEqpCheckPlanDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑点检计划明细
export const editEqpCheckPlanDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 点检目标计划列表
export const eqpCheckPlanTargetPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TargetPage, params });
};

// 删除点检目标计划
export const deleteEqpCheckPlanTargetApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.TargetDelete + id,
  });
};

// 保存点检目标计划
export const addEqpCheckPlanTargetApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.TargetAdd, params });
};

// 编辑点检目标计划
export const editEqpCheckPlanTargetApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.TargetEdit, params });
};

// 按类别新增点检目标计划
export const addEqpCheckPlanTargetByCategoryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.TargetAddByCategory, params });
};

// 按类别新增点检计划明细
export const addEqpCheckPlanDetailByCategoryApi = (planId: string, categoryId: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAddByCategory + planId + '/' + categoryId });
};

// 启用点检计划
export const enableEqpCheckPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Enable + planId });
};

// 作废点检计划
export const disableEqpCheckPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Disable + planId });
};

// 停止点检计划
export const pauseEqpCheckPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Pause + planId });
};

// 恢复点检计划
export const recoverEqpCheckPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Recover + planId });
};

// 复制点检计划
export const copyEqpCheckPlanApi = (planId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Copy + planId });
};

// 点检计划明细选项列表
export const eqpCheckPlanDetailParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamPage, params });
};

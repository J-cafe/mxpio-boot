import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotcheck/tpmCheckTask/list',
  Page = '/erp/equipment/spotcheck/tpmCheckTask/page',
  Delete = '/erp/equipment/spotcheck/tpmCheckTask/delete/',
  Add = '/erp/equipment/spotcheck/tpmCheckTask/add',
  Edit = '/erp/equipment/spotcheck/tpmCheckTask/edit',
  DetailPage = '/erp/equipment/spotcheck/tpmCheckTask/detail/page',
  DetailList = '/erp/equipment/spotcheck/tpmCheckTask/detail/list',
  DetailDelete = '/erp/equipment/spotcheck/tpmCheckTask/detail/delete/',
  DetailAdd = '/erp/equipment/spotcheck/tpmCheckTask/detail/add',
  DetailEdit = '/erp/equipment/spotcheck/tpmCheckTask/detail/edit',
  Release = '/erp/equipment/spotcheck/tpmCheckTask/release/',
  Dispatch = '/erp/equipment/spotcheck/tpmCheckTask/dispatch/',
  Grab = '/erp/equipment/spotcheck/tpmCheckTask/grab/',
  Veto = '/erp/equipment/spotcheck/tpmCheckTask/veto/',
  DetailParamPage = '/erp/equipment/check/task/detail/param/page',
  DetailParamList = '/erp/equipment/check/task/detail/param/list_by_parent/',
  TransferPage = '/erp/equipment/spotcheck/tpmCheckTask/transfer/page',
  Resend = '/erp/equipment/spotcheck/tpmCheckTask/reSend/',
  Execute = '/erp/equipment/spotcheck/tpmCheckTask/excut',
}

/**
 * @description: 设备点检任务列表
 */
export const eqpCheckTaskListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备点检任务列表
 */
export const eqpCheckTaskPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备点检任务
 */

export function deleteEqpCheckTaskApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备点检任务
 */

export const addEqpCheckTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpCheckTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 设备点检任务明细列表
export const eqpCheckTaskDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 设备点检任务明细列表
export const eqpCheckTaskDetailListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailList, params });
};

// 删除设备点检任务明细
export const deleteEqpCheckTaskDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备点检任务明细
 */
export const addEqpCheckTaskDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑点检任务明细
export const editEqpCheckTaskDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 点检任务下达
export const releaseEqpCheckTaskApi = (id: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Release + id });
};

// 点检任务派工
export const dispatchEqpCheckTaskApi = (bizNo: string, username: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Dispatch + bizNo + '/' + username });
};

// 点检任务转派
export const resendEqpCheckTaskApi = (bizNo: string, username: string, reason: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Resend + bizNo + '/' + username + '/' + reason });
};

// 点检任务抢单
export const grabEqpCheckTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Grab, params });
};

// 点检任务否决
export const rejectEqpCheckTaskApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Veto + bizNo, params });
};

// 点检任务明细参数列表分页
export const eqpCheckTaskDetailParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamPage, params });
};

// 点检任务明细参数列表
export const eqpCheckTaskDetailParamListApi = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamList + id });
};

// 点检任务执行
export const eqpCheckTaskExecuteApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute, params });
};

// 点检任务转单列表
export const eqpCheckTaskTransferPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TransferPage, params });
};

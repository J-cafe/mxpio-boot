import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/task/list',
  Page = '/erp/equipment/upkeep/task/page',
  Delete = '/erp/equipment/upkeep/task/remove/',
  Add = '/erp/equipment/upkeep/task/add',
  Edit = '/erp/equipment/upkeep/task/edit',
  AdminEdit = '/erp/equipment/upkeep/task/admin/edit',
  DetailPage = '/erp/equipment/upkeep/task/detail/page',
  DetailList = '/erp/equipment/upkeep/task/detail/list',
  DetailDelete = '/erp/equipment/upkeep/task/detail/remove/',
  DetailAdd = '/erp/equipment/upkeep/task/detail/add',
  DetailEdit = '/erp/equipment/upkeep/task/detail/edit',
  Release = '/erp/equipment/upkeep/task/release/',
  Dispatch = '/erp/equipment/upkeep/task/dispatch/',
  Grab = '/erp/equipment/upkeep/task/grab/',
  Veto = '/erp/equipment/upkeep/task/veto/batch/',
  DetailParamPage = '/erp/equipment/upkeep/task/detail/param/page',
  DetailParamList = '/erp/equipment/upkeep/task/detail/param/list_by_parent/',
  TransferPage = '/erp/equipment/upkeep/task/transfer/page',
  Resend = '/erp/equipment/upkeep/task/reSend/',
  Execute = '/erp/equipment/upkeep/task/excut',
  Submit = '/erp/equipment/upkeep/task/submit',
  StartExecute = '/erp/equipment/upkeep/task/startExcut/',
  RecordPage = '/erp/equipment/upkeep/task/operation/page',
  AssistSave = '/erp/equipment/upkeep/task/assistants/save',
  AssistPage = '/erp/equipment/upkeep/task/assistants/page',
  ProgressAdd = '/erp/equipment/upkeep/task/progress/add',
  ProgressPage = '/erp/equipment/upkeep/task/progress/page',
  Stop = '/erp/equipment/upkeep/task/stop',
  End = '/erp/equipment/upkeep/task/end/',
  Picking = '/erp/equipment/upkeep/task/excute/pickup/',
  PickingList = '/erp/equipment/upkeep/task/pickup/list/',
  Return = '/erp/equipment/upkeep/task/excute/return/',
  RateNo = '/erp/equipment/upkeep/task/evaluate/no',
  RateYes = '/erp/equipment/upkeep/task/evaluate/yes',
  RateList = '/erp/equipment/upkeep/task/evaluate/list',
}

/**
 * @description: 设备保养任务列表
 */
export const eqpUpkeepTaskListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备保养任务列表
 */
export const eqpUpkeepTaskPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备保养任务
 */

export function deleteEqpUpkeepTaskApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备保养任务
 */

export const addEqpUpkeepTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpUpkeepTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 管理员编辑保养任务
export const adminEditEqpUpkeepTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.AdminEdit, params });
};

// 设备保养任务明细列表
export const eqpUpkeepDetailPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailPage, params });
};

// 设备保养任务明细列表
export const eqpUpkeepDetailListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailList, params });
};

// 删除设备保养任务明细
export const deleteEqpUpkeepDetailApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DetailDelete + id,
  });
};

/**
 * @description: 保存设备保养任务明细
 */
export const addEqpUpkeepDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.DetailAdd, params });
};

// 编辑保养任务明细
export const editEqpUpkeepDetailApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.DetailEdit, params });
};

// 保养任务下达
export const releaseEqpUpkeepApi = (id: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Release + id });
};

// 保养任务派工
export const dispatchEqpUpkeepApi = (bizNo: string, username: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Dispatch + bizNo + '/' + username });
};

// 保养任务转派
export const resendEqpUpkeepApi = (bizNo: string, username: string, reason: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Resend + bizNo + '/' + username + '/' + reason });
};

// 保养任务抢单
export const grabEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Grab, params });
};

// 保养任务否决
export const rejectEqpUpkeepApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Veto + bizNo, params });
};

// 保养任务明细参数列表分页
export const eqpUpkeepParamPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamPage, params });
};

// 保养任务明细参数列表
export const eqpUpkeepDetailParamListApi = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.DetailParamList + id });
};

// 保养任务完成
export const executeEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Execute, params });
};

// 保养任务提交
export const submitEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Submit, params });
};

// 保养任务转单列表
export const transferEqpUpkeepPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TransferPage, params });
};

// 保养任务开始执行
export const startEqpUpkeepApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.StartExecute + bizNo });
};

// 保养任务操作记录列表
export const recordEqpUpkeepPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RecordPage, params });
};

// 保养任务协助人保存
export const assistSaveEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AssistSave, params });
};

// 保养任务协助人列表
export const assistPageEqpUpkeepApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AssistPage, params });
};

// 保养任务进度保存
export const progressAddEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ProgressAdd, params });
};

// 保养任务进度列表
export const progressPageEqpUpkeepApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ProgressPage, params });
};

// 保养任务暂停
export const stopEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Stop, params });
};

// 保养任务恢复
export const endEqpUpkeepApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.End + bizNo });
};

// 保养任务领料
export const pickingEqpUpkeepApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Picking + bizNo, params });
};

// 保养任务领料列表
export const pickingEqpUpkeepListApi = (bizNo: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.PickingList + bizNo, params });
};

// 保养任务退料
export const returnEqpUpkeepApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Return + bizNo, params });
};

// 保养任务评价-否
export const rateEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.RateNo, params });
};

// 保养任务评价-是
export const rateYesEqpUpkeepApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.RateYes, params });
};

// 保养任务评价列表
export const rateEqpUpkeepListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RateList, params });
};

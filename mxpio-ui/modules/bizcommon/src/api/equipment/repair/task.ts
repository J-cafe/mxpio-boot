import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotrepair/list',
  Page = '/erp/equipment/spotrepair/page',
  Delete = '/erp/equipment/spotrepair/delete/',
  Add = '/erp/equipment/spotrepair/add',
  Edit = '/erp/equipment/spotrepair/edit',
  Detail = '/erp/equipment/spotrepair/code/',
  AdminEdit = '/erp/equipment/spotrepair/admin/edit',
  Send = '/erp/equipment/spotrepair/send',
  Veto = '/erp/equipment/spotrepair/veto/batch/',
  TransferPage = '/erp/equipment/spotrepair/transfer/page',
  Resend = '/erp/equipment/spotrepair/reSend',
  Finish = '/erp/equipment/spotrepair/finish',
  Submit = '/erp/equipment/spotrepair/submit',
  Start = '/erp/equipment/spotrepair/start',
  Check = '/erp/equipment/spotrepair/check',
  RecordPage = '/erp/equipment/spotrepair/listTpmRepairOperationByMainId',
  AssistSave = '/erp/equipment/spotrepair/assistants/save',
  AssistPage = '/erp/equipment/spotrepair/assistants/page',
  ProgressAdd = '/erp/equipment/spotrepair/progress/add',
  ProgressPage = '/erp/equipment/spotrepair/progress/page',
  Stop = '/erp/equipment/spotrepair/stop',
  End = '/erp/equipment/spotrepair/end',
  Update = '/erp/equipment/spotrepair/change',
  Picking = '/erp/equipment/spotrepair/excute/pickup/',
  PickingList = '/erp/equipment/spotrepair/pickup/list/',
  Return = '/erp/equipment/spotrepair/excute/return/',
  RateNo = '/erp/equipment/spotrepair/isEvaluate',
  RateYes = '/erp/equipment/spotrepair/evaluate/detail/save',
  RateList = '/erp/equipment/spotrepair/evaluate/detail/list',
}

/**
 * @description: 设备维修任务列表
 */
export const eqpRepairTaskListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备维修任务列表
 */
export const eqpRepairTaskPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备维修任务
 */

export function deleteEqpRepairTaskApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 保存设备维修任务
 */
export const addEqpRepairTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpRepairTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

export const getRepairByBizNoApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Detail + bizNo });
};

// 管理员编辑维修任务
export const adminEditEqpRepairTaskApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.AdminEdit, params });
};

// 维修任务派工、抢单
export const sendEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Send, params });
};

// 维修任务转派
export const resendEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Resend, params });
};

// 维修任务否决
export const rejectEqpRepairApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Veto + bizNo, params });
};

// 维修任务完成
export const finishEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish, params });
};

// 维修任务提交
export const submitEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Submit, params });
};

export const checkEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Check, params });
};

// 维修任务转单列表
export const transferEqpRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TransferPage, params });
};

// 维修任务开始执行
export const startEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Start, params });
};

// 维修任务操作记录列表
export const recordEqpRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RecordPage, params });
};

// 维修任务协助人保存
export const assistSaveEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AssistSave, params });
};

// 维修任务协助人列表
export const assistEqpRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AssistPage, params });
};

// 维修任务进度保存
export const progressAddEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ProgressAdd, params });
};

// 维修任务进度列表
export const progressPageEqpRepairApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ProgressPage, params });
};

// 维修任务暂停
export const stopEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Stop, params });
};

// 维修任务恢复
export const endEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.End, params });
};

// 维修任务变更
export const updateEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Update, params });
};

// 维修任务领料
export const pickingEqpRepairApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Picking + bizNo, params });
};

// 维修任务领料列表
export const pickingEqpRepairListApi = (bizNo: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.PickingList + bizNo, params });
};

// 维修任务退料
export const returnEqpRepairApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Return + bizNo, params });
};

// 维修任务评价-否
export const rateNoEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.RateNo, params });
};

// 维修任务评价-是
export const rateYesEqpRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.RateYes, params });
};

// 维修任务评价列表
export const rateEqpRepairListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RateList, params });
};

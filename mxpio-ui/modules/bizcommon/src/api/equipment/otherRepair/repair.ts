import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/other/repair/list',
  Page = '/erp/equipment/other/repair/page',
  Delete = '/erp/equipment/other/repair/remove/',
  Add = '/erp/equipment/other/repair/add',
  Edit = '/erp/equipment/other/repair/edit',
  Send = '/erp/equipment/other/repair/send',
  Outsource = '/erp/equipment/other/repair/add/outsource',
  RecordList = '/erp/equipment/other/repair/operation/list',
  Start = '/erp/equipment/other/repair/start',
  Resend = '/erp/equipment/other/repair/reSend',
  AddProgress = '/erp/equipment/other/repair/progress/add',
  ProgressPage = '/erp/equipment/other/repair/progress/page',
  Close = '/erp/equipment/other/repair/shutDown',
  Picking = '/erp/equipment/other/repair/excute/pickup/',
  PickingList = '/erp/equipment/other/repair/pickup/list/',
  TransferList = '/erp/equipment/other/repair/transfer/list/',
  Return = '/erp/equipment/other/repair/excute/return/',
  Finish = '/erp/equipment/other/repair/finish',
  Check = '/erp/equipment/other/repair/check',
  AssistSave = '/erp/equipment/other/assistants/save',
  AssistPage = '/erp/equipment/other/assistants/page',
  AssistDelete = '/erp/equipment/other/assistants/remove/',
}

/**
 * @description: 其他维修类型列表
 */

export const otherRepairListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 其他维修类型列表
 */

export const otherRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除其他维修类型
 */

export function deleteOtherRepairApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存其他维修类型
 */

export const addOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 编辑其他维修类型
 */

export const editOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 抢单或派单接口
export const sendOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Send, params });
};

// 其他维修转委外接口
export const outsourceOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Outsource, params });
};

// 操作记录列表接口
export const recordListRepairApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RecordList, params });
};

// 开始接口
export const startOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Start, params });
};

// 转派接口
export const resendOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Resend, params });
};

// 上报进度接口
export const addProgressOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddProgress, params });
};

// 进度列表接口
export const progressOtherRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ProgressPage, params });
};

// 关闭接口
export const closeOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Close, params });
};

// 备件领用接口
export const pickingOtherRepairApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Picking + bizNo, params });
};

// 备件退货接口
export const returnOtherRepairApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Return + bizNo, params });
};

// 根据维修单号获取领用列表接口
export const pickingOtherRepairLineApi = (bizNo: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.PickingList + bizNo, params });
};

// 转派列表接口
export const transferOtherRepairListApi = (bizNo: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.TransferList + bizNo, params });
};

// 完工接口
export const finishOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Finish, params });
};

// 确认接口
export const checkOtherRepairApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Check, params });
};

// 协助接口
export const assistSaveOtherRepairApi = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.put({ url: Api.AssistSave, params });
};

// 协助列表接口
export const assistOtherRepairPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.AssistPage, params });
};

// 协助删除接口
export const assistDeleteOtherRepairApi = (id: string) => {
  const { http } = useBridge();
  return http.delete({ url: Api.AssistDelete + id });
};

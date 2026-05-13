// 维修委外
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/spotrepair/outsource/list',
  Page = '/erp/equipment/spotrepair/outsource/page',
  Add = '/erp/equipment/spotrepair/outsource/add',
  RemarkPage = '/erp/equipment/spotrepair/outsource/remark/page',
  DeleteRemark = '/erp/equipment/spotrepair/outsource/remark/delete/',
  AddRemark = '/erp/equipment/spotrepair/outsource/remark/add',
  EditRemark = '/erp/equipment/spotrepair/outsource/remark/edit',
  InquiryPage = '/erp/equipment/spotrepair/outsource/inquiry/page',
  InquiryAdopt = '/erp/equipment/spotrepair/outsource/inquiry/adopt',
  InquiryRemove = '/erp/equipment/spotrepair/outsource/inquiry/remove/',
  InquiryEdit = '/erp/equipment/spotrepair/outsource/inquiry/edit',
  InquiryAdd = '/erp/equipment/spotrepair/outsource/inquiry/add',
  Issued = '/erp/equipment/spotrepair/outsource/issued',
  Reject = '/erp/equipment/spotrepair/outsource/reject',
  InquiryEnd = '/erp/equipment/spotrepair/outsource/inquiry/end/',
}

/**
 * @description: 维修委外列表
 */

export const repairOutsourceListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 维修委外列表
 */
export const repairOutsourcePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

// 维修委外新增
export const addRepairOutsourceApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 维修委外备注列表
 */
export const repairOutsourceRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RemarkPage, params });
};

// 删除维修委外备注
export function deleteRepairOutsourceRemarkApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteRemark + id,
  });
}

// 保存维修委外备注
export const addRepairOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddRemark, params });
};

// 编辑维修委外备注
export const editRepairOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.EditRemark, params });
};

// 维修委外询价列表
export const repairOutsourceInquiryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.InquiryPage, params });
};

// 维修委外询价确认
export const repairOutsourceInquiryAdoptApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdopt, params });
};

// 删除维修委外询价
export function deleteRepairOutsourceInquiryApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.InquiryRemove + id,
  });
}

// 编辑维修委外询价
export const editRepairOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.InquiryEdit, params });
};

// 保存维修委外询价
export const addRepairOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdd, params });
};

// 下达
export const repairOutsourceIssuedApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Issued, params });
};

// 否决
export const repairOutsourceRejectApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Reject, params });
};

// 询价完成
export const repairOutsourceInquiryEndApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryEnd + bizNo });
};

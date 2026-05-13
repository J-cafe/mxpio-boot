// 保养领料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/task/outsource/list',
  Page = '/erp/equipment/upkeep/task/outsource/page',
  Add = '/erp/equipment/upkeep/task/outsource/add',
  RemarkPage = '/erp/equipment/upkeep/task/outsource/remark/page',
  DeleteRemark = '/erp/equipment/upkeep/task/outsource/remark/delete/',
  AddRemark = '/erp/equipment/upkeep/task/outsource/remark/add',
  EditRemark = '/erp/equipment/upkeep/task/outsource/remark/edit',
  InquiryPage = '/erp/equipment/upkeep/task/outsource/inquiry/page',
  InquiryAdopt = '/erp/equipment/upkeep/task/outsource/inquiry/adopt',
  InquiryRemove = '/erp/equipment/upkeep/task/outsource/inquiry/remove/',
  InquiryEdit = '/erp/equipment/upkeep/task/outsource/inquiry/edit',
  InquiryAdd = '/erp/equipment/upkeep/task/outsource/inquiry/add',
  Issued = '/erp/equipment/upkeep/task/outsource/issued',
  Reject = '/erp/equipment/upkeep/task/outsource/reject',
  InquiryEnd = '/erp/equipment/upkeep/task/outsource/inquiry/end/',
}

/**
 * @description: 保养委外列表
 */

export const upkeepOutsourceListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 保养委外列表
 */
export const upkeepOutsourcePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

// 保养委外新增
export const addUpkeepOutsourceApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 保养委外备注列表
 */
export const upkeepOutsourceRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RemarkPage, params });
};

// 删除保养委外备注
export function deleteUpkeepOutsourceRemarkApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteRemark + id,
  });
}

// 保存保养委外备注
export const addUpkeepOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddRemark, params });
};

// 编辑保养委外备注
export const editUpkeepOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.EditRemark, params });
};

// 保养委外询价列表
export const upkeepOutsourceInquiryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.InquiryPage, params });
};

// 保养委外询价确认
export const upkeepOutsourceInquiryAdoptApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdopt, params });
};

// 删除保养委外询价
export function deleteUpkeepOutsourceInquiryApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.InquiryRemove + id,
  });
}

// 编辑保养委外询价
export const editUpkeepOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.InquiryEdit, params });
};

// 保存保养委外询价
export const addUpkeepOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdd, params });
};

// 下达
export const upkeepOutsourceIssuedApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Issued, params });
};

// 否决
export const upkeepOutsourceRejectApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Reject, params });
};

// 询价完成
export const upkeepOutsourceInquiryEndApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryEnd + bizNo });
};

// 其他维修领料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/other/repair/outsource/list',
  Page = '/erp/equipment/other/repair/outsource/page',
  RemarkPage = '/erp/equipment/other/repair/outsource/remark/page',
  DeleteRemark = '/erp/equipment/other/repair/outsource/remark/delete/',
  AddRemark = '/erp/equipment/other/repair/outsource/remark/add',
  EditRemark = '/erp/equipment/other/repair/outsource/remark/edit',
  InquiryPage = '/erp/equipment/other/repair/outsource/inquiry/page',
  InquiryAdopt = '/erp/equipment/other/repair/outsource/inquiry/adopt',
  InquiryRemove = '/erp/equipment/other/repair/outsource/inquiry/remove/',
  InquiryEdit = '/erp/equipment/other/repair/outsource/inquiry/edit',
  InquiryAdd = '/erp/equipment/other/repair/outsource/inquiry/add',
  Issued = '/erp/equipment/other/repair/outsource/issued',
  Reject = '/erp/equipment/other/repair/outsource/reject',
  InquiryEnd = '/erp/equipment/other/repair/outsource/inquiry/end/',
}

/**
 * @description: 其他维修委外列表
 */

export const otherOutsourceListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 其他维修委外列表
 */
export const otherOutsourcePageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 其他维修委外列表
 */
export const otherOutsourceRemarkPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.RemarkPage, params });
};

// 删除其他维修委外备注
export function deleteOtherOutsourceRemarkApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteRemark + id,
  });
}

// 保存其他维修委外备注
export const addOtherOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddRemark, params });
};

// 编辑其他维修委外备注
export const editOtherOutsourceRemarkApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.EditRemark, params });
};

// 其他维修委外询价列表
export const otherOutsourceInquiryPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.InquiryPage, params });
};

// 其他维修委外询价确认
export const otherOutsourceInquiryAdoptApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdopt, params });
};

// 删除其他维修委外询价
export function deleteOtherOutsourceInquiryApi(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.InquiryRemove + id,
  });
}

// 编辑其他维修委外询价
export const editOtherOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.InquiryEdit, params });
};

// 保存其他维修委外询价
export const addOtherOutsourceInquiryApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryAdd, params });
};

// 下达
export const otherOutsourceIssuedApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Issued, params });
};

// 否决
export const otherOutsourceRejectApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Reject, params });
};

// 询价完成
export const otherOutsourceInquiryEndApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.InquiryEnd + bizNo });
};

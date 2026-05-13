import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/ib/list',
  Page = '/erp/quality/ib/page',
  Delete = '/erp/quality/ib/remove/',
  Config = '/erp/common/res/list/QC1',
  Save = '/erp/quality/ib/save',
  Judge = '/erp/quality/ib/judge/',
  IbdetectList = '/erp/quality/ibdetect/list/',
  IbsimpleList = '/erp/quality/ibsimple/list/',
  IbsimpleDetailList = '/erp/quality/ibcheckdetail/listbysimple/',
  IbdetectDetailList = '/erp/quality/ibcheckdetail/listbydetect/',
  Check = '/erp/quality/ibcheckdetail/check/',
  Generate = '/erp/quality/ib/generate/',
  ByCode = '/erp/quality/ib/list/',
  Record = '/erp/quality/ib/record/page',
  Unqualified = '/erp/quality/product/unqualified/page',
}

/**
 * @description: 质检单列表
 */
export const qualityListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const qualityPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

// 根据单号获取质检单
export const qualityByCodeApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ByCode + bizNo });
};

// 质检项目明细
export const ibdetectListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.IbdetectList + bizNo });
};

// 质检项目检测明细
export const ibdetectDetailListApi = (bizNo: string, detectCode: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.IbsimpleDetailList}${bizNo}/${detectCode}` });
};

// 质检样本明细
export const ibsimpleListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.IbsimpleList + bizNo });
};

// 质检样本检测明细
export const ibsimpleDetailListApi = (bizNo: string, simpleCode: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.IbsimpleDetailList}${bizNo}/${simpleCode}` });
};

/**
 * @description: 质检结果保存
 */
export const qualityCheckApi = (bizNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Check + bizNo, params });
};

/**
 * @description: 删除质检单
 */
export function qualityDeleteApi(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 质检单配置
 */
export const qualityConfigApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存质检单
 */
export const qualitySaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 确认质检单
 */
export const qualityJudgeApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Judge + bizNo });
};

/**
 * @description: 质检单确认质检数量
 */
export const qualityGenerateApi = (bizNo: string, checkQuantity: number) => {
  const { http } = useBridge();
  return http.put({ url: `${Api.Generate}${bizNo}/${checkQuantity}` });
};

export const qualityRecordPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Record, params });
};

export const qualityUnqualifiedPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Unqualified, params });
};

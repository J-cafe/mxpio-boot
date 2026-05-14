import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/purc/supply/list',
  Page = '/erp/purc/supply/page',
  Delete = '/erp/purc/supply/remove/',
  Config = '/erp/common/res/list/Supply',
  Add = '/erp/purc/supply/add',
  Edit = '/erp/purc/supply/edit',
  Assign = '/erp/purc/supply/assign/',
  Transfer = '/erp/purc/supply/transfer/',
}

/**
 * @description: 供应商列表
 */

export const supplyList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

export const supplyPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除供应商
 */

export function deleteSupply(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 供应商配置
 */

export const supplyConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 新增供应商
 */

export const addSupply = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 供应商分配
 */

export const supplyAssign = (bizMan: string, pnCodes: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Assign + bizMan + '/' + pnCodes });
};

/**
 * @description: 供应商转移
 */

export const supplyTransfer = (fromBizMan: string, toBizMan: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Transfer + fromBizMan + '/' + toBizMan });
};

/**
 * @description: 编辑供应商
 */

export function editSupply(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

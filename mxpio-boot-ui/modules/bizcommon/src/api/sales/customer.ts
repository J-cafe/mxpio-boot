import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/sales/cst/list',
  Page = '/erp/sales/cst/page',
  Delete = '/erp/sales/cst/remove/',
  Config = '/erp/common/res/list/Customer',
  Save = '/erp/sales/cst/save',
  Assign = '/erp/sales/cst/assign/',
  Transfer = '/erp/sales/cst/transfer/',
  Address = '/erp/sales/address/list/bypn/',
}

/**
 * @description: 客户列表
 */

export const customerList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 客户分页
 */
export const customerPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const customerAddress = (pnCode: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Address + pnCode });
};

/**
 * @description: 删除客户
 */

export function deleteCustomer(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 客户配置
 */

export const customerConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 保存客户
 */

export const saveCustomer = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 客户分配
 */

export const customerAssign = (bizMan: string, pnCodes: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Assign + bizMan + '/' + pnCodes });
};

/**
 * @description: 客户转移
 */

export const customerTransfer = (fromBizMan: string, toBizMan: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Transfer + fromBizMan + '/' + toBizMan });
};

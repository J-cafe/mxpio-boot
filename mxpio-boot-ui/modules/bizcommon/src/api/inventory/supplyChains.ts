import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Item = '/erp/inventory/supplychain/item/page',
  Supply = '/erp/inventory/supplychain/supply/page',
  ItemRemove = '/erp/inventory/supplychain/item/remove/',
  SupplyRemove = '/erp/inventory/supplychain/supply/remove/',
  ItemWithout = '/erp/inventory/supplychain/without/items/page/',
  SupplyWithout = '/erp/inventory/supplychain/without/supplies/page/',
  Save = '/erp/inventory/supplychain/save',
  ItemEdit = '/erp/inventory/supplychain/item/edit/',
  SupplyEdit = '/erp/inventory/supplychain/supply/edit/',
  SupplychainItem = '/erp/inventory/supplychain/supplyno/itempage/',
  SupplychainByItem = '/erp/inventory/supplychain/supply/list/',
}

/**
 * @description: 供应链按物料
 */
export const supplychainItemPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Item, params });
};

/**
 * @description: 供应链按供应商
 */
export const supplychainSupplyPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Supply, params });
};

/**
 * @description: 未配物料
 */
export const supplychainItemWithout = (pnCode: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemWithout + pnCode, params });
};

/**
 * @description: 未配供应商
 */
export const supplychainSupplyWithout = (itemCode: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SupplyWithout + itemCode, params });
};

/**
 * @description: 按物料删除关联
 */

export function supplychainItemRemove(itemCode: string, pnCodes: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.ItemRemove + itemCode + '/' + pnCodes,
  });
}

/**
 * @description: 按供应商删除关联
 */
export function supplychainSupplyRemove(itemCode: string, pnCodes: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.SupplyRemove + itemCode + '/' + pnCodes,
  });
}

/**
 * @description: 保存关联关系
 */

export const supplychainSave = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 批量修改供应链
 */
export const batchEditByItem = (itemCode: string, pnCodes: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.ItemEdit + encodeURIComponent(itemCode) + '/' + pnCodes, params });
};

/**
 * @description: 批量修改供应链
 */
export const batchEditBySupply = (pnCodes: string, itemCode: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.SupplyEdit + pnCodes + '/' + itemCode, params });
};

/**
 * @description: 查看供应商关联物料
 */
export const itemPageBySupply = (pnCode: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SupplychainItem + pnCode, params });
};

/**
 * @description: 查看物料相同供应商
 */
export const supplyListByItem = (pnCode: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.SupplychainByItem + pnCode, params });
};

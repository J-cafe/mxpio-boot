import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  ItemWithin = '/erp/quality/ischain/scheme/itempage/',
  ItemGroupsWithin = '/erp/quality/ischain/scheme/itemgrouppage/',
  TemplateWithin = '/erp/quality/ischain/getschemelistbypath/',
  ChainDelete = '/erp/quality/ischain/scheme/remove/',
  ItemWithOut = '/erp/quality/ischain/without/items/page/',
  ItemGroupWithOut = '/erp/quality/ischain/without/itemgroups/page/',
  ChainSave = '/erp/quality/ischain/save',
}

/**
 * @description: 方案已关联物料
 */
export const qtChainItemWithinApi = (code: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemWithin + code, params });
};

/**
 * @description: 方案已关联物料组
 */
export const qtChainItemGroupsWithinApi = (code: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemGroupsWithin + code, params });
};

/**
 * @description: 已关联方案
 */
export const qtChainTemplateWithinApi = (
  code: string,
  datascope: string,
  params?: RequestCriteriaParams,
) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.TemplateWithin}${code}/${datascope}`, params });
};

/**
 * @description: 删除关联
 */

export function qtChainRemoveApi(code: string, ids: string, type: string) {
  const { http } = useBridge();
  return http.delete({
    url: `${Api.ChainDelete}/${code}/${ids}/${type}`,
  });
}

/**
 * @description: 未配物料
 */
export const qtChainItemWithoutApi = (busiType: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemWithOut + busiType, params });
};

/**
 * @description: 未关联物料组
 */
export const qtChainItemGroupWithoutApi = (code: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ItemGroupWithOut + code, params });
};

/**
 * @description: 保存关联
 */
export const saveQtChainApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.ChainSave, params });
};

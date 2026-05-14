import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/list',
  Page = '/erp/equipment/page',
  Delete = '/erp/equipment/remove/',
  Add = '/erp/equipment/add',
  Edit = '/erp/equipment/edit',
  Sync = '/erp/equipment/batch_sync/',
  AllSync = '/erp/equipment/all_sync',
}

/**
 * @description: 设备列表
 */
export const eqpInfoListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备列表
 */
export const eqpInfoPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备
 */
export function deleteEqpInfoApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存设备
 */
export const addEqpInfoApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: 更新设备
 */
export const editEqpInfoApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

// 按设备编码同步更新设备
export const syncEqpInfoApi = (eqpCode: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Sync + eqpCode });
};

// 同步更新设备
export const allSyncEqpInfoApi = () => {
  const { http } = useBridge();
  return http.put({ url: Api.AllSync });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/area/list',
  Page = '/erp/equipment/area/page',
  Tree = '/erp/equipment/area/tree',
  Delete = '/erp/equipment/area/remove/',
  Add = '/erp/equipment/area/add',
  Edit = '/erp/equipment/area/edit',
  ChildList = '/erp/equipment/area/childList/',
}

/**
 * @description: 区域列表
 */

export const areaListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 区域列表
 */

export const areaPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

export const areaTreeApi = () => {
  const { http } = useBridge();
  return http.get({ url: Api.Tree });
};

export const areaChildListApi = (pid: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ChildList + pid });
};

/**
 * @description: 删除区域
 */

export function deleteAreaApi(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存区域
 */

export const addAreaApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editAreaApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Delete = '/res/url/remove',
  List = '/res/url/list',
  Add = '/res/url/add',
  Edit = '/res/url/edit',
  ResDataList = '/res/data/list/',
  AddAuth = '/res/data/add',
  EditAuth = '/res/data/edit',
  DeleteAuth = '/res/data/remove/',
  ApiList = '/res/data/api/list',
  GetDataFilterList = '/datafilter/res/list/',
  AddDataFilter = '/datafilter/add',
  EditDataFilter = '/datafilter/edit',
  DeleteDataFilter = '/datafilter/remove/',
}

/**
 * @description: Get menu list based
 */

export const menuList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Delete menu based on id
 */

export const deleteMenu = (params: { id: string }) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete,
    params,
  });
};

export const addMenu = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit post based
 */

export function editMenu(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: Get auth list based
 */

export const resDataList = (urlId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ResDataList + urlId });
};

/**
 * @description: Delete auth based on id
 */

export const deleteAuth = (ids: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteAuth + ids,
  });
};

/**
 * @description: Add auth based
 */
export const addAuth = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddAuth, params });
};

/**
 * @description: Edit auth based
 */

export function editAuth(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditAuth, params });
}

/**
 * @description: Get api list based
 */

export const apiList = () => {
  const { http } = useBridge();
  return http.get({ url: Api.ApiList });
};

/**
 * @description: 获取数据过滤列表
 */
// export const datafilterList = (authId: string) => {
//   return defHttp.get({ url: Api.GetDataFilterList + authId });
// };

/**
 * @description: 删除数据过滤
 */

export const deleteDatafilter = (ids: string) => {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteDataFilter + ids,
  });
};

/**
 * @description: 新增数据过滤
 */
export const addDatafilter = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddDataFilter, params });
};

/**
 * @description: 编辑数据过滤
 */

export function editDatafilter(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditDataFilter, params });
}

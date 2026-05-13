// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/sys/dict/tree/page',
  Add = '/sys/dict/add',
  Edit = '/sys/dict/edit',
  Delete = '/sys/dict/remove/',
  DeleteItem = '/sys/dict/remove/item/',
  BasicUrl = '/sys/dict/',
}

/**
 * @description: Get dict list based
 */

export const dictList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add dict based
 */

export const addDict = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit dict based
 */

export function editDict(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete dict based
 */

export function deleteDict(ids: String) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: Add dict based
 */

export const addDictItem = (code: String, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: `${Api.BasicUrl}${code}/add`, params });
};

/**
 * @description: Edit dict based
 */

export function editDictItem(code: String, params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: `${Api.BasicUrl}${code}/edit`, params });
}

/**
 * @description: delete dict based
 */

export function deleteDictItem(id: String) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteItem + id,
  });
}

/**
 * @description: Get dict item list based
 */
export const getDictByCode = (code: String) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.BasicUrl}tree/${code}` });
};

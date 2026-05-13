import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/posttype/page',
  Add = '/posttype/add',
  Edit = '/posttype/edit',
  Delete = '/posttype/remove/',
}

/**
 * @description: Get post list based
 */

export const posttypeList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add post based
 */

export const addPosttype = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit post based
 */

export function editPosttype(params: Recordable[]) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete post based
 */

export function deletePosttype(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

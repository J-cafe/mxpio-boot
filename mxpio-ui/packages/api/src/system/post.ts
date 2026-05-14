import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/post/page',
  Add = '/post/add',
  Edit = '/post/edit',
  Delete = '/post/remove/',
}

/**
 * @description: Get post list based
 */

export const postList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add post based
 */

export const addPost = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit post based
 */

export function editPost(params: Recordable[]) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete post based
 */

export function deletePost(ids: String) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

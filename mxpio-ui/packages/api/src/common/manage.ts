// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';

/**
 * @description: Get
 */

export const getAction = (url: string, params: Recordable) => {
  const { http } = useBridge();
  return http.get({ url, params });
};

/**
 * @description: Post
 */

export const postAction = (url: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url, params });
};

/**
 * @description: Put
 */

export function putAction(url: string, params: Recordable) {
  const { http } = useBridge();
  return http.put({ url, params });
}

/**
 * @description: Delete
 */

export function deleteAction(url: string, params: Recordable) {
  const { http } = useBridge();
  return http.delete({
    url,
    params,
  });
}

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  Read = '/message/read/',
  MyUnread = '/message/myUnread/innerMsg',
}

// 为@mxpio/layouts提供的接口，避免后续模块频繁升级
/**
 * @description: read message based
 */

export const readMessage = (type: string, id?: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.Read}${type}${id ? '/' + id : ''}` });
};

/**
 * @description: Get message list based
 */

export const myUnread = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.MyUnread, params });
};

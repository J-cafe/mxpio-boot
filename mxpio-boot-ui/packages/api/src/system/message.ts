import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/message/my/',
  Read = '/message/read/',
  Channel = '/message/channel/list',
  Send = '/message/send',
  MyUnread = '/message/myUnread/innerMsg',
}

/**
 * @description: Get message list based
 */

export const messageList = (type: string, params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List + type, params });
};

/**
 * @description: read message based
 */

export const readMessage = (type: string, id?: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.Read}${type}${id ? '/' + id : ''}` });
};

/**
 * @description: send message based
 */

export function sendMessage(params: {
  channel: string;
  from: string;
  to: string;
  title: string;
  content: string;
}) {
  const { http } = useBridge();
  return http.post({ url: Api.Send, params });
}

/**
 * @description: Get message list based
 */

export const myUnread = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.MyUnread, params });
};

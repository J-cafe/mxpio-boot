import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/common/order/list',
  Page = '/erp/common/order/page',
  Delete = '/erp/common/order/remove/',
  Add = '/erp/common/order/add',
  Edit = '/erp/common/order/edit',
  Config = '/erp/common/order/list/',
}

/**
 * @description: 列表
 */

export const orderList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 列表
 */

export const orderPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除
 */

export function deleteOrder(code: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + code,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 报错
 */

export const addOrder = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Add, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 报错
 */

export const editOrder = (params: Recordable) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'message',
    },
  );
};

export const commonConfig = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config + code });
};

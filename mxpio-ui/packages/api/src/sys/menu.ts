import { useBridge } from '@mxpio/bridge';
import { getMenuListResultModel } from './model/menuModel';

enum Api {
  GetMenuList = '/permiss/url/list',
}

/**
 * @description: Get user menu based on id
 */

export const getMenuList = () => {
  const { http } = useBridge();
  return http.get<getMenuListResultModel>({ url: Api.GetMenuList });
};

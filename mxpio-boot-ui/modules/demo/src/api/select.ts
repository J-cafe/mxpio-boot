import { useBridge } from '@mxpio/bridge';
import { DemoOptionsItem, selectParams } from './model/optionsModel';

enum Api {
  OPTIONS_LIST = '/select/getDemoOptions',
}

/**
 * @description: Get sample options value
 */
export const optionsListApi = (params?: selectParams) => {
  const { http } = useBridge();
  return http.get<DemoOptionsItem[]>({ url: Api.OPTIONS_LIST, params });
};

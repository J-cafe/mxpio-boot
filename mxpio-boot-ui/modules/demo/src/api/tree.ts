// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';

enum Api {
  TREE_OPTIONS_LIST = '/tree/getDemoOptions',
}

/**
 * @description: Get sample options value
 */
export const treeOptionsListApi = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get<Recordable[]>({ url: Api.TREE_OPTIONS_LIST, params });
};

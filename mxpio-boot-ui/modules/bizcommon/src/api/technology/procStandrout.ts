import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/standrout/list',
  Page = '/erp/tech/standrout/page',
  Delete = '/erp/tech/standrout/remove/',
  Save = '/erp/tech/standrout/save',
  ProcStandroutLineList = '/erp/tech/standrout/proc/list/',
}

/**
 * @description: 标准工艺列表
 */
export const procStandroutList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 标准工艺列表
 */
export const procStandroutPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 获取标准工艺工序列表
 */
export function procStandroutLineList(code: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.ProcStandroutLineList + code,
  });
}

/**
 * @description: 删除标准工艺
 */
export function deleteProcStandrout(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增标准工艺
 */
export const saveProcStandrout = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/prodrout/procfixture/list',
  Page = '/erp/tech/prodrout/procfixture/page',
  Delete = '/erp/tech/prodrout/procfixture/remove/',
  Save = '/erp/tech/prodrout/procfixture/save',
}

/**
 * @description:  工艺参数设备列表
 */

export const procEqpList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 工艺参数设备列表
 */

export const procEqpPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除工艺参数设备
 */

export function deleteProcEqp(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增工艺参数设备
 */

export const saveProcEqp = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

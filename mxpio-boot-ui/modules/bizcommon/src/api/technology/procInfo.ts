import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/procinfo/list',
  Page = '/erp/tech/procinfo/page',
  Delete = '/erp/tech/procinfo/remove/',
  Config = '/erp/common/res/list/ProcessInfo',
  Save = '/erp/tech/procinfo/save',
}

/**
 * @description: 工序列表
 */

export const procInfoList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 工序列表
 */

export const procInfoPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 工序配置
 */

export const procInfoConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除工序
 */

export function deleteProcInfo(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增工序
 */

export const saveProcInfo = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

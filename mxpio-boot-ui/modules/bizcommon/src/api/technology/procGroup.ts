import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/procgroup/list',
  Page = '/erp/tech/procgroup/page',
  Delete = '/erp/tech/procgroup/remove/',
  // Config = '/erp/common/res/list/ProcessInfo',
  Save = '/erp/tech/procgroup/save',
  ProcGroupLineList = '/erp/tech/procgroupline/list/',
}

/**
 * @description: 工序段列表
 */
export const procGroupList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 工序段列表
 */
export const procGroupPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 获取工序段工序列表
 */
export function procGroupLineList(code: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.ProcGroupLineList + code,
  });
}

// /**
//  * @description: 工序段配置
//  */
// export const procGroupConfig = (params?: Recordable) => {
//   const { http } = useBridge();
//   return http.get({ url: Api.Config, params });
// };

/**
 * @description: 删除工序段
 */
export function deleteProcGroup(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增工序段
 */
export const saveProcGroup = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

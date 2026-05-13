import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/workteam/list',
  Page = '/erp/tech/workteam/page',
  Delete = '/erp/tech/workteam/remove/',
  Config = '/erp/common/res/list/Workteam',
  Save = '/erp/tech/workteam/save',
  WorkteamMember = '/erp/tech/workteam/member/list/',
}

/**
 * @description: 班组列表
 */

export const workteamList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 班组列表
 */

export const workteamPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 班组配置
 */

export const workteamConfig = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Config, params });
};

/**
 * @description: 删除班组
 */

export function deleteWorkteam(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 获取班组成员
 */
export function getWorkteamMember(code: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.WorkteamMember + code,
  });
}

/**
 * @description: 报错班组
 */

export const saveWorkteam = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

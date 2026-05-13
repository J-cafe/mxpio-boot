import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/group/list',
  Page = '/erp/equipment/group/page',
  Delete = '/erp/equipment/group/remove/',
  Add = '/erp/equipment/group/add',
  Edit = '/erp/equipment/group/edit',
  Member = '/erp/equipment/group/detail/list/',
  MemberByCode = '/erp/equipment/group/detail/listByCode/',
}

/**
 * @description: 设备班组列表
 */

export const eqpGroupList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 设备班组列表
 */

export const eqpGroupPage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除设备班组
 */

export function deleteEqpGroup(id: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + id,
  });
}

/**
 * @description: 获取设备班组成员
 */
export function getEqpGroupMember(id: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.Member + id,
  });
}

/**
 * @description: 获取设备班组成员（根据班组编码）
 */
export function getEqpGroupMemberByCode(code: string) {
  const { http } = useBridge();
  return http.get({
    url: Api.MemberByCode + code,
  });
}

/**
 * @description: 保存设备班组
 */

export const addEqpGroup = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEqpGroup = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';
import type { Key } from 'ant-design-vue/lib/table/interface';

enum Api {
  List = '/role/page',
  Add = '/role/add',
  Edit = '/role/edit',
  Delete = '/role/remove/',
  Basic = '/role/list/',
  DeptWithinRole = '/dept/role/within/',
  DeptWithoutRole = '/dept/role/without/',
  PostWithinRole = '/post/role/within/',
  PostWithoutRole = '/post/role/without/',
  ResList = '/res/list',
  PermissList = '/permiss/list',
  SavePermiss = '/permiss/add/batch',
  Datafilter = '/datafilter/res/list/',
  SaveRoleDatafilter = '/datafilter/role/add/batch',
  RoleWithinDatafilter = '/datafilter/role/within/',
}

/**
 * @description: Get Role list based
 */

export const roleList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Role based
 */

export const addRole = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Role based
 */

export function editRole(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Role based
 */

export function deleteRole(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 按角色查关联用户
 */

export const roleWithinUser = (roleId: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.Basic}${roleId}/within` });
};

/**
 * @description: 按角色查未关联用户
 */

export const roleWithoutUser = (roleId: string, params: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.Basic}${roleId}/without`, params });
};

/**
 * @description: 按角色查关联部门
 */

export const roleWithinDept = (roleId: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.DeptWithinRole}${roleId}` });
};

/**
 * @description: 按角色查未关联部门
 */

export const roleWithoutDept = (roleId: string, params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.DeptWithoutRole}${roleId}`, params });
};

/**
 * @description: 按角色查关联岗位
 */

export const roleWithinPost = (roleId: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.PostWithinRole}${roleId}` });
};

/**
 * @description: 按角色查未关联岗位
 */

export const roleWithoutPost = (roleId: string, params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.PostWithoutRole}${roleId}`, params });
};

/**
 * @description: 新增角色演员
 */

export const addRoleActors = (roleId: string, params: Key[]) => {
  const { http } = useBridge();
  return http.post({ url: `${Api.Basic}${roleId}/add/actors`, params });
};

/**
 * @description: 删除角色演员
 */

export const deleteRoleActors = (roleId: string, params: string[]) => {
  const { http } = useBridge();
  return http.post({ url: `${Api.Basic}${roleId}/remove/actors`, params });
};

/**
 * @description: 获取全部菜单及权限
 */

export const resList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ResList, params });
};

/**
 * @description: 根据角色获取分配的菜单及权限
 */

export const permisList = (params?: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.PermissList, params });
};

/**
 * @description: 保存角色菜单及权限
 */
export const savePermiss = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.SavePermiss, params });
};

/**
 * @description: 根据权限获取对应的过滤规则
 */

export const datafilterList = (resId: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Datafilter + resId });
};

/**
 * @description: 保存角色的过滤规则
 */
export const saveRoleDatafilter = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.SaveRoleDatafilter, params });
};

/**
 * @description: 获取角色对应的过滤规则
 */

export const roleWithinDatafilter = (resId: string, roleId: string) => {
  const { http } = useBridge();
  return http.get({ url: `${Api.RoleWithinDatafilter}${resId}/${roleId}` });
};

// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/dept/tree',
  Add = '/dept/add',
  Edit = '/dept/edit',
  Delete = '/dept/remove/',
  withinUser = '/dept/user/within/',
  withoutUser = '/dept/user/without/',
  AddDeptUser = '/dept/user/add',
  DeleteDeptUser = '/dept/user/delete/',
}

/**
 * @description: Get Dept list based
 */

export const deptList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Dept based
 */

export const addDept = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Dept based
 */

export function editDept(params: Recordable[]) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Dept based
 */

export function deleteDept(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 按部门查关联用户
 */

export const deptWithinUser = (deptId: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.withinUser + deptId, params });
};

/**
 * @description: 按部门查关联用户
 */

export const deptWithoutUser = (deptId: string, params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.withoutUser + deptId, params });
};

/**
 * @description: 新增部门用户
 */

export const addDeptUser = (params: Recordable[]) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddDeptUser, params });
};

/**
 * @description: 删除部门用户
 */

export const deleteDeptUser = (deptId: string, userId: string) => {
  const { http } = useBridge();
  return http.delete({ url: `${Api.DeleteDeptUser}${deptId}/${userId}` });
};

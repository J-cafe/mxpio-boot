import { useBridge } from '@mxpio/bridge';

enum Api {
  GetUserList = '/user/list',
  AddUser = '/user/add',
  EditUser = '/user/edit',
  DeleteUser = '/user/delete/',
  GetUserInfo = '/user/info',
}

/**
 * @description: Get user based
 */

export const getUserList = (params: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.GetUserList, params });
};

/**
 * @description: Add user based
 */

export const addUser = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddUser, params });
};

/**
 * @description: Edit user based
 */

export function editUser(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.EditUser, params });
}

/**
 * @description: delete user based
 */

export function deleteUser(params: Recordable) {
  const { http } = useBridge();
  return http.delete({
    url: Api.DeleteUser,
    params,
  });
}

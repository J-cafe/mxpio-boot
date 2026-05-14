// import { defHttp } from '@mxpio/request';

import type { RequestCriteriaParams } from '@mxpio/types';
import { useBridge } from '@mxpio/bridge';

enum Api {
  List = '/user/profile/list',
  Add = '/user/profile/add',
  Edit = '/user/profile/edit',
}

/**
 * @description: Get Profile list based
 */
export const profileList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Profile based
 */

export const addProfile = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Add, params },
    {
      successMessageMode: 'none',
    },
  );
};

/**
 * @description: Edit Profile based
 */

export function editProfile(params: Recordable) {
  const { http } = useBridge();
  return http.put(
    { url: Api.Edit, params },
    {
      successMessageMode: 'none',
    },
  );
}

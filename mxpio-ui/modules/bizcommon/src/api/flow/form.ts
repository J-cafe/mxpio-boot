// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/camunda/form/page',
  Delete = '/camunda/flow/remove/',
  Deploy = '/camunda/form/deploy/',
  Add = '/camunda/form/add',
  Edit = '/camunda/form/edit',
}

/**
 * @description: Get Form list based
 */

export const formList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Form based
 */

export const addForm = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Form based
 */

export function editForm(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Form based
 */

export function deleteForm(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 发布表单
 */

export const deployForm = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Deploy + code });
};

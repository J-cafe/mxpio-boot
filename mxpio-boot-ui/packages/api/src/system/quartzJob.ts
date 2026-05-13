import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/quartz/page',
  Add = '/quartz/add',
  Edit = '/quartz/edit',
  Delete = '/quartz/delete/',
  Pause = '/quartz/pause/',
  Run = '/quartz/run/',
  Execute = '/quartz/execute/',
}

/**
 * @description: Get job list based
 */

export const quartzList = (params: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add job based
 */

export const addQuartz = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit job based
 */

export function editQuartz(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete job based
 */

export function deleteQuartz(ids: String) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: run job based
 */

export const runQuartz = (id: String) => {
  const { http } = useBridge();
  return http.post({ url: Api.Run + id });
};

/**
 * @description: run job based
 */

export const pauseQuartz = (id: String) => {
  const { http } = useBridge();
  return http.post({ url: Api.Pause + id });
};

/**
 * @description: Get job list based
 */

export const executeQuartz = (params: Recordable) => {
  const { http } = useBridge();
  return http.get({ url: Api.Execute, params });
};

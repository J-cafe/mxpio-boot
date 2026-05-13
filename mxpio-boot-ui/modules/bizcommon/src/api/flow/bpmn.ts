// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/camunda/flow/page',
  Delete = '/camunda/flow/remove/',
  Deploy = '/camunda/flow/deploy/',
  Start = '/camunda/process/start/',
  Def = '/camunda/process/def/',
  Details = '/camunda/process/details/',
  Add = '/camunda/flow/add',
  Edit = '/camunda/flow/edit',
}

/**
 * @description: Get Bpmn list based
 */

export const bpmnList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Bpmn based
 */

export const addBpmn = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Bpmn based
 */

export function editBpmn(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Bpmn based
 */

export function deleteBpmn(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 部署流程
 */

export const deployBpmn = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Deploy + code });
};

/**
 * @description: 发起流程
 */

export const startBpmn = (code: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Start + code, params });
};

/**
 * @description: 查询流程最新版本呢信息
 */

export const defBpmn = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Def + code });
};

/**
 * @description: 按流程示例查询流程任务信息
 */

export const defDetails = (code: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Details + code });
};

// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/camunda/task/all/page',
  Complete = '/camunda/task/complete/', // 审核通过
  Rejectlast = '/camunda/task/reject/last/', // 退回上一节点
  Rejectfirst = '/camunda/task/reject/first/', // 退回发起人
  Claim = '/camunda/task/claim/', // 拾取
  Historics = '/camunda/task/historics/', // 审批流程数据
  Active = '/camunda/task/active/', //当前节点
  Cancel = '/camunda/process/cancel/', //撤回
  FormModel = '/camunda/task/form/model/', // 按任务id获取表单模型
  Finished = '/camunda/task/finished/page',
  My = '/camunda/process/page/my',
  Suspend = '/camunda/process/suspend/',
  Start = '/camunda/process/restart/',
  Urgent = '/camunda/process/urgent/',
  ProcessList = '/camunda/process/page',
}

/**
 * @description: 获取待办任务接口
 */

export const taskList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 获取已完成任务接口
 */

export const finishedList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Finished, params });
};

/**
 * @description: 获取我的申请接口
 */

export const myApplyList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.My, params });
};

/**
 * @description: 按任务id获取节点表单
 */
export const getFormModel = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.FormModel + id });
};

/**
 * @description: 获取流程实例节点办理记录
 */
export const getHistorics = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Historics + id });
};

/**
 * @description: 获取流程实例当前激活任务
 */
export const getActive = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Active + id });
};

/**
 * @description:拾取
 */

export const claimTask = (id: string) => {
  const { http } = useBridge();
  return http.post({ url: Api.Claim + id });
};

/**
 * @description: 签核、同意流程
 */

export const completeTask = (id: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Complete + id, params });
};

/**
 * @description: 驳回上一节点流程
 */

export const rejectlastTask = (id: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Rejectlast + id, params });
};

/**
 * @description: 不同意流程
 */

export const rejectfirstTask = (id: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Rejectfirst + id, params });
};

/**
 * @description: 加急流程
 */

export const urgent = (id: string, params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Urgent + id, params });
};

/**
 * @description: 暂停当前流程
 */
export const suspend = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Suspend + id });
};

/**
 * @description: 开始当前流程
 */
export const start = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Start + id });
};

/**
 * @description: 暂停当前流程
 */
export const processList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.ProcessList, params });
};

/**
 * 根据ID取消对应的流程/任务
 * @param {string} id - 要取消的流程/任务ID
 * @returns {Promise} 包含取消请求结果的Promise
 * @throws {Error} 当请求失败时抛出错误
 */
export const cancelTask = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Cancel + id });
};

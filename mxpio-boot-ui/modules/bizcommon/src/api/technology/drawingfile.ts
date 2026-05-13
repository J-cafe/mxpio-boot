import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/tech/prodrout/proc/drawingfile/list',
  ListByProc = '/erp/tech/prodrout/proc/drawingfile/list_by_proc/',
  Delete = '/erp/tech/prodrout/proc/drawingfile/remove/',
  Add = '/erp/tech/prodrout/proc/drawingfile/add',
  SignIn = '/erp/tech/prodrout/proc/drawingfile/sign_in_apply',
  SignOutCancle = '/erp/tech/prodrout/proc/drawingfile/sign_out_cancle/',
  SignOut = '/erp/tech/prodrout/proc/drawingfile/sign_out/',
}

/**
 * @description:  工艺图纸列表
 */

export const drawingfileList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 工艺图纸列表（根据工艺工序ID和版本）
 */
export const drawingfileListByProc = (routProcId: string, version: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ListByProc + routProcId + '/' + version });
};

/**
 * @description: 删除工艺图纸
 */

export function deleteDrawingfile(code: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + code,
  });
}

/**
 * @description: 新增工艺图纸
 */

export const addDrawingfile = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

// 签入图纸
export const signInDrawingfile = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.SignIn, params });
};

// 签出图纸
export const signOutDrawingfile = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.SignOut + id });
};

// 取消签出
export const signOutCancleDrawingfile = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.SignOutCancle + id });
};

// 不良品处理单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/quality/ud/list',
  Page = '/erp/quality/ud/page',
  Delete = '/erp/quality/ud/remove/',
  Qoline = '/erp/quality/udline/list/',
  Save = '/erp/quality/ud/save',
  SaveAndPass = '/erp/quality/ud/save_and_pass',
  Submit = '/erp/quality/ud/submit/',
}

/**
 * @description: 不良品处理单列表
 */

export const udListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 不良品处理单分页
 */
export const udPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 不良品处理单行列表
 */
export const udLineListApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Qoline + bizNo });
};

/**
 * @description: 删除
 */

export function udDeleteApi(bizNo: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + bizNo,
  });
}

/**
 * @description: 保存并通过
 */

export const udSaveAndPassApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.SaveAndPass, params });
};

/**
 * @description: 保存
 */
export const udSaveApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Save, params });
};

/**
 * @description: 提交审核
 */
export const udSubmitApi = (bizNo: string) => {
  const { http } = useBridge();
  return http.put({ url: Api.Submit + bizNo });
};

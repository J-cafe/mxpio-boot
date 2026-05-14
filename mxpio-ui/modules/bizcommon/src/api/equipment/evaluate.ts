import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/evaluate/list',
  Page = '/erp/equipment/evaluate/page',
  Delete = '/erp/equipment/evaluate/remove/',
  Add = '/erp/equipment/evaluate/add',
  Edit = '/erp/equipment/evaluate/edit',
}

/**
 * @description: 评价列表
 */

export const evaluateListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 评价列表
 */

export const evaluatePage = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 删除评价
 */

export function deleteEvaluate(typeCode: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + typeCode,
  });
}

/**
 * @description: 保存评价
 */

export const addEvaluate = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

export const editEvaluate = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
};

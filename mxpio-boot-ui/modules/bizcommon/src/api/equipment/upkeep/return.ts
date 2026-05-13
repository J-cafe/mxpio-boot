// 维修退料通知单
import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/erp/equipment/upkeep/returnnotice/list',
  Page = '/erp/equipment/upkeep/returnnotice/page',
  Delete = '/erp/equipment/upkeep/returnnotice/remove/',
  Reject = '/erp/equipment/upkeep/returnnotice/reject/',
  Execute = '/erp/equipment/upkeep/returnnotice/execute/',
  Line = '/erp/equipment/upkeep/returnnotice/line/list/',
  Save = '/erp/equipment/upkeep/returnnotice/save',
}

/**
 * @description: 维修退料通知单列表
 */

export const eqpUpkeepReturnListApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: 维修退料通知单分页
 */
export const eqpUpkeepReturnPageApi = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.Page, params });
};

/**
 * @description: 维修退料通知单行列表
 */
export const eqpUpkeepReturnLineApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.Line + noticeNo });
};

/**
 * @description: 删除
 */

export function deleteEqpUpkeepReturnApi(noticeNo: string) {
  const { http } = useBridge();
  return http.delete(
    {
      url: Api.Delete + noticeNo,
    },
    {
      successMessageMode: 'message',
    },
  );
}

/**
 * @description: 拒绝
 */

export const eqpUpkeepReturnRejectApi = (noticeNo: string) => {
  const { http } = useBridge();
  return http.put(
    { url: Api.Reject + noticeNo },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 执行
 */
export const eqpUpkeepReturnExecuteApi = (noticeNo: string, params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Execute + noticeNo, params },
    {
      successMessageMode: 'message',
    },
  );
};

/**
 * @description: 新增
 */
export const saveEqpUpkeepReturnApi = (params: Recordable) => {
  const { http } = useBridge();
  return http.post(
    { url: Api.Save, params },
    {
      successMessageMode: 'message',
    },
  );
};

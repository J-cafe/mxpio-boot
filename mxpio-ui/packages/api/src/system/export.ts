import { useBridge } from '@mxpio/bridge';
import type { RequestCriteriaParams } from '@mxpio/types';

enum Api {
  List = '/excel/export/solution/page',
  Add = '/excel/export/solution/add',
  Edit = '/excel/export/solution/edit',
  Delete = '/excel/export/solution/remove/',
  ExportColumns = '/excel/export/solution/columns/',
  AddExportColumn = '/excel/export/solution/column/add',
  EditExportColumn = '/excel/export/solution/column/edit',
  DeleteExportColumn = '/excel/export/solution/column/remove/',
  ExportDownload = 'excel/export/download',
}

/**
 * @description: Get Export list based
 */

export const exportList = (params?: RequestCriteriaParams) => {
  const { http } = useBridge();
  return http.get({ url: Api.List, params });
};

/**
 * @description: Add Export based
 */

export const addExport = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.Add, params });
};

/**
 * @description: Edit Export based
 */

export function editExport(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Edit, params });
}

/**
 * @description: delete Export based
 */

export function deleteExport(ids: string) {
  const { http } = useBridge();
  return http.delete({
    url: Api.Delete + ids,
  });
}

/**
 * @description: 按模板id查询导出字段
 */

export const exportColumnsList = (id: string) => {
  const { http } = useBridge();
  return http.get({ url: Api.ExportColumns + id });
};

/**
 * @description: 新增导出字段
 */

export const addExportColumn = (params: Recordable) => {
  const { http } = useBridge();
  return http.post({ url: Api.AddExportColumn, params });
};

/**
 * @description: 编辑导出字段
 */

export const editExportColumn = (params: Recordable) => {
  const { http } = useBridge();
  return http.put({ url: Api.EditExportColumn, params });
};

/**
 * @description: 删除导出字段
 */

export const deleteExportColumn = (id: string) => {
  const { http } = useBridge();
  return http.delete({ url: `${Api.DeleteExportColumn}${id}` });
};

/**
 * 导出文件的API调用函数。
 *
 * @param type 导出文件格式。
 * @param code 导出文件编码。
 * @returns 返回一个Promise对象，成功时返回文件下载的字节流。
 */
export function exportDownload(
  code: string,
  type: string = 'xls',
  params?: { [key: string]: any },
) {
  const { http } = useBridge();
  return http.get(
    {
      url: `${Api.ExportDownload}/${type}/${code}`,
      params,
      timeout: 5 * 60 * 1000,
      responseType: 'blob',
    },
    { isReturnNativeResponse: true }, //返回原生响应，包含响应头
  );
}

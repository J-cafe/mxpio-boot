import { UploadApiResult } from './model/uploadModel';
import { useBridge } from '@mxpio/bridge';
import { UploadFileParams, AxiosProgressEvent } from '@mxpio/types';

enum Api {
  UploadUrl = '/file/upload',
  Download = '/file/download/',
  FileInfo = '/file/getFileInfo/',
}

/**
 * @description: Upload interface
 */
export function uploadApi(
  params: UploadFileParams,
  onUploadProgress: (progressEvent: AxiosProgressEvent) => void,
) {
  const { http } = useBridge();
  return http.uploadFile<UploadApiResult>(
    {
      url: `${Api.UploadUrl}`,
      onUploadProgress,
    },
    params,
  );
}

/**
 * 下载文件的API调用函数。
 *
 * @param fileNo 文件编号，用于拼接下载链接。
 * @returns 返回一个Promise对象，成功时返回文件下载的字节流。
 */
export function downloadApi(fileNo: string) {
  const { http } = useBridge();
  return http.get(
    { url: Api.Download + fileNo, timeout: 5 * 60 * 1000, responseType: 'blob' },
    { isReturnNativeResponse: true }, //返回原生响应，包含响应头
  );
}

/**
 * 获取文件信息
 * @param fileNo 文件编号，用于拼接请求URL
 * @return 返回通过HTTP GET请求获取的文件信息
 */
export function getFileInfo(fileNo: string) {
  const { http } = useBridge();
  return http.get({ url: Api.FileInfo + fileNo });
}

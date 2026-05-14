import { downloadApi, duplicateCheck } from '@mxpio/api';
import { downloadByData } from '@mxpio/utils/src/file/download';

import { useMessage } from './useMessage';

const { createMessage } = useMessage();
/**
 * 通用方法
 * @returns
 */
export function useCommon() {
  /**
   * 校验数据的唯一性
   * @param params 传递给 duplicateCheck 函数的参数
   * @returns 返回一个 Promise 对象，如果校验通过，则解析为一个空对象{}，如果校验失败，则拒绝并返回错误信息
   */
  function duplicateCheckCommon(params) {
    return new Promise((resolve, reject) => {
      // 调用 duplicateCheck 函数进行重复性检查
      duplicateCheck(params)
        .then((res) => {
          // 如果返回值为 1，表示数据已存在，校验失败
          if (res === 1) {
            return reject('编码已存在');
          }
          // 校验通过，返回空对象
          return resolve({});
        })
        .catch((err) => {
          // 捕获 duplicateCheck 函数的错误，返回错误信息
          reject(err.message || '验证失败');
        });
    });
  }

  function downloadByFileNo(fileNo: string, fileName?: string) {
    if (!fileNo) {
      return createMessage.error('文件不存在');
    }
    createMessage.loading('下载中...');
    downloadApi(fileNo).then((res) => {
      const { data, headers } = res;
      if (!fileName) {
        fileName = decodeURIComponent(
          headers['content-disposition'].split(';')[1].split('filename=')[1],
        );
        // fileName字符串带""时，截取名称
        if (fileName.indexOf('"') === 0) {
          fileName = fileName.slice(1, fileName.length - 1);
        }
      }
      downloadByData(data, fileName);
      setTimeout(() => {
        createMessage.destroy();
      }, 200);
    });
  }

  return {
    duplicateCheck: duplicateCheckCommon,
    downloadByFileNo,
  };
}

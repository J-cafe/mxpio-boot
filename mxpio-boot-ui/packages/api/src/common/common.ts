import { useBridge } from '@mxpio/bridge';

enum Api {
  Duplicate = '/common/duplicate/',
  Sngenerate = '/common/sngenerate',
}

/**
 * 系统通用校验重复方法
 * @param {*} param {tableName}/{column}/{key}
 * @returns
 */
export function duplicateCheck(param: {
  tableName: string;
  column: string;
  key: string;
  exclude?: string;
}) {
  const { http } = useBridge();
  return http.get({
    url: `${Api.Duplicate}${param.tableName}/${param.column}/${param.key}`,
    params: {
      exclude: param.exclude,
    },
  });
}

/**
 * 系统通用单号生成
 * @param {*} snExpression
 * @returns
 */
export function sngenerate(snExpression: string) {
  const { http } = useBridge();
  return http.post({
    url: Api.Sngenerate,
    params: {
      snExpression: snExpression,
    },
  });
}

// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import { AreaModel, AreaParams } from './model/areaModel';

enum Api {
  AREA_RECORD = '/cascader/getAreaRecord',
}

export const areaRecord = (data: AreaParams) => {
  const { http } = useBridge();
  return http.post<AreaModel[]>({ url: Api.AREA_RECORD, data });
};

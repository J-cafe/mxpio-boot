// import { defHttp } from '@mxpio/request';
import { useBridge } from '@mxpio/bridge';
import { GetAccountInfoModel } from './model/accountModel';

enum Api {
  ACCOUNT_INFO = '/account/getAccountInfo',
  SESSION_TIMEOUT = '/user/sessionTimeout',
  TOKEN_EXPIRED = '/user/tokenExpired',
}

// Get personal center-basic settings

export const accountInfoApi = () => {
  const { http } = useBridge();
  return http.get<GetAccountInfoModel>({ url: Api.ACCOUNT_INFO });
};

export const sessionTimeoutApi = () => {
  const { http } = useBridge();
  return http.post<void>({ url: Api.SESSION_TIMEOUT });
};

export const tokenExpiredApi = () => {
  const { http } = useBridge();
  return http.post<void>({ url: Api.TOKEN_EXPIRED });
};

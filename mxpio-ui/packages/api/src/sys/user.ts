import { useBridge } from '@mxpio/bridge';
import { LoginParams, LoginResultModel, GetUserInfoModel, PermResult } from './model/userModel';
import type { ErrorMessageMode } from '@mxpio/types';

enum Api {
  Login = '/login',
  Logout = '/logout',
  GetUserInfo = '/user/info', //获取登录用户信息
  GetPermCode = '/permiss/data/list', //获取用户权限
  TestRetry = '/testRetry',
  Captcha = '/captcha',
  RefreshToken = '/token/refresh',
  SystemConfig = '/loadConfigResource',
  Updatepass = '/user/updatepass',
  Updatepwdwithcheck = '/user/updatepwdwithcheck',
}

/**
 * @description: user login api
 */
export function loginApi(params: LoginParams, mode: ErrorMessageMode = 'modal') {
  // const http = resolve<VAxiosOptions>(HTTP_TOKEN);
  const { http } = useBridge();
  return http.post<LoginResultModel>(
    {
      url: Api.Login,
      params,
    },
    {
      errorMessageMode: mode,
      successMessageMode: 'none',
    },
  );
}

/**
 * @description: getUserInfo
 */
export function getUserInfo() {
  const { http } = useBridge();
  return http.get<GetUserInfoModel>({ url: Api.GetUserInfo }, { errorMessageMode: 'none' });
}

export function getPermCode() {
  const { http } = useBridge();
  return http.get<PermResult>({ url: Api.GetPermCode });
}

export function doLogout() {
  const { http } = useBridge();
  return http.get({ url: Api.Logout });
}

export function testRetry() {
  const { http } = useBridge();
  return http.get(
    { url: Api.TestRetry },
    {
      retryRequest: {
        isOpenRetry: true,
        count: 5,
        waitTime: 1000,
      },
    },
  );
}

/**
 * 获取验证码
 * @returns {Promise} 返回一个Promise对象，该对象的值是获取验证码的响应数据
 */
export function getCaptcha() {
  const { http } = useBridge();
  return http.get({ url: Api.Captcha }, { isTransformResponse: false });
}

export function refreshToken(params: any) {
  const { http } = useBridge();
  return http.post(
    {
      url: Api.RefreshToken,
      // 后端 refreshToken(@RequestBody TokenVo) 要求 JSON body
      data: params,
    },
    {
      errorMessageMode: 'none',
      successMessageMode: 'none',
    },
  );
}

/**
 * 更新密码的接口调用函数
 * @param params
 * @returns 返回通过defHttp.put方法发起的更新密码请求的Promise对象
 */
export function updatepass(params: Recordable) {
  const { http } = useBridge();
  return http.put({ url: Api.Updatepass, params });
}

/**
 * 更新密码，带校验功能。
 * @param params 包含新密码、旧密码和用户名的对象。
 * @param params.newPassword 新密码。
 * @param params.oldPassword 旧密码。
 * @param params.username 用户名。
 * @returns 返回一个 Promise 对象，代表异步操作的结果。
 */
export function updatepwdwithcheck(params: {
  newPassword: string;
  oldPassword: string;
  username: string;
}) {
  const { http } = useBridge();
  // 使用 defHttp 的 put 方法发送更新密码的请求
  return http.put({ url: Api.Updatepwdwithcheck, params });
}

/**
 * @description: 获取系统配置信息
 */
export function getSystemConfig() {
  const { http } = useBridge();
  return http.get({
    url: Api.SystemConfig,
  });
}

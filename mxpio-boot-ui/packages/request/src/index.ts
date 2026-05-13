// axios配置  可自行根据项目进行更改，只需更改该文件即可，其他文件可以不动
// The axios configuration can be changed according to the project, just change the file, other files can be left unchanged

import type { AxiosInstance, AxiosResponse } from 'axios';
import { clone } from 'lodash-es';
import type { RequestOptions, Result, RequestQueue } from '@mxpio/types';
import type { AxiosTransform, CreateAxiosOptions } from './axiosTransform';
import { VAxios } from './Axios';
import { checkStatus } from './checkStatus';
// import { useMessage } from '@mxpio/hooks';
import {
  isString,
  isUndefined,
  isNull,
  isEmpty,
  getToken,
  setObjToUrlParams,
  deepMerge,
} from '@mxpio/utils';
import { $t } from '@mxpio/locales';
import { RequestEnum, ResultEnum, ContentTypeEnum } from '@mxpio/enums';
import { joinTimestamp, formatRequestDate } from './helper';
import { AxiosRetry } from './axiosRetry';
import axios from 'axios';
import { useBridge } from '@mxpio/bridge';
import { useUserStoreWithOut, useErrorLogStoreWithOut } from '@mxpio/stores';
import { Modal, message as Message } from 'ant-design-vue';

// const { createMessage, createErrorModal, createSuccessModal } = useMessage();

/**
 * @description: 数据处理，方便区分多种处理方式
 */
const transform: AxiosTransform = {
  /**
   * @description: 处理响应数据。如果数据不是预期格式，可直接抛出错误
   */
  transformResponseHook: (res: AxiosResponse<Result>, options: RequestOptions) => {
    const { isTransformResponse, isReturnNativeResponse } = options;
    // 是否返回原生响应头 比如：需要获取响应头时使用该属性
    if (isReturnNativeResponse) {
      return res;
    }
    // 不进行任何处理，直接返回
    // 用于页面代码可能需要直接获取code，data，message这些信息时开启
    if (!isTransformResponse) {
      return res.data;
    }
    // 错误的时候返回

    const { data, config } = res;
    if (!data) {
      // return '[HTTP] Request has no return value';
      throw new Error($t('sys.api.apiRequestFailed'));
    }
    //  这里 code，result，message为 后台统一的字段，需要在 types.ts内修改为项目自己的接口返回格式
    const { code, result, message } = data;

    // 这里逻辑可以根据项目进行修改
    const hasSuccess = data && Reflect.has(data, 'code') && code === ResultEnum.SUCCESS;

    if (hasSuccess) {
      let successMsg = message;

      if (isNull(successMsg) || isUndefined(successMsg) || isEmpty(successMsg)) {
        successMsg = $t(`sys.api.operationSuccess`);
      }
      if (options.successMessageMode === 'modal') {
        Modal.success({ title: $t('sys.api.successTip'), content: successMsg });
      } else if (
        options.successMessageMode === 'message' ||
        (['post', 'put', 'delete'].includes(config.method as string) &&
          options.successMessageMode === undefined) // 默认'post', 'put', 'delete' 弹出成功消息
      ) {
        Message.success(successMsg);
      }
      return result;
    }

    // 在此处根据自己项目的实际情况对不同的code执行不同的操作
    // 如果不希望中断当前请求，请return数据，否则直接抛出异常即可
    let timeoutMsg = '';
    switch (code) {
      case ResultEnum.TIMEOUT:
        timeoutMsg = $t('sys.api.timeoutMessage');
        // const useUserStoreWithOut = useStore('useUserStoreWithOut');
        const userStore = useUserStoreWithOut();
        userStore.logout(true);
        break;
      default:
        if (message) {
          timeoutMsg = message;
        }
    }

    // errorMessageMode='modal'的时候会显示modal错误弹窗，而不是消息提示，用于一些比较重要的错误
    // errorMessageMode='none' 一般是调用时明确表示不希望自动弹出错误提示
    if (options.errorMessageMode === 'modal') {
      // createErrorModal({ title: t('sys.api.errorTip'), content: timeoutMsg });
      Modal.error({ title: $t('sys.api.errorTip'), content: timeoutMsg });
    } else if (options.errorMessageMode === 'message') {
      Message.error(timeoutMsg);
    }

    throw new Error(timeoutMsg || $t('sys.api.apiRequestFailed'));
  },

  // 请求之前处理config
  beforeRequestHook: (config, options) => {
    const { joinPrefix, joinParamsToUrl, formatDate, joinTime = true, urlPrefix } = options;

    if (joinPrefix) {
      config.url = `${urlPrefix}${config.url}`;
    }

    // if (apiUrl && isString(apiUrl)) {
    //   config.url = `${apiUrl}${config.url}`;
    // }

    const params = config.params || {};
    const data = config.data || false;
    formatDate && data && !isString(data) && formatRequestDate(data);
    if (config.method?.toUpperCase() === RequestEnum.GET) {
      if (!isString(params)) {
        // 给 get 请求加上时间戳参数，避免从缓存中拿数据。
        config.params = Object.assign(params || {}, joinTimestamp(joinTime, false));
      } else {
        // 兼容restful风格
        config.url = config.url + params + `${joinTimestamp(joinTime, true)}`;
        config.params = undefined;
      }
    } else {
      if (!isString(params)) {
        formatDate && formatRequestDate(params);
        if (
          Reflect.has(config, 'data') &&
          config.data &&
          (Object.keys(config.data).length > 0 || config.data instanceof FormData)
        ) {
          config.data = data;
          config.params = params;
        } else {
          // 非GET请求如果没有提供data，则将params视为data
          config.data = params;
          config.params = undefined;
        }
        if (joinParamsToUrl) {
          config.url = setObjToUrlParams(
            config.url as string,
            Object.assign({}, config.params, config.data),
          );
        }
      } else {
        // 兼容restful风格
        config.url = config.url + params;
        config.params = undefined;
      }
    }
    return config;
  },

  /**
   * @description: 请求拦截器处理
   */
  requestInterceptors: (config, options) => {
    // 请求之前处理config
    const token = getToken();
    if (token && (config as Recordable)?.requestOptions?.withToken !== false) {
      // jwt token
      (config as Recordable).headers['Access-Token'] = options.authenticationScheme
        ? `${options.authenticationScheme} ${token}`
        : token;
    }
    const { router } = useBridge();
    // 添加全局的请求头,接口权限控制使用
    config.params = {
      _key: router.currentRoute?.value.meta?.key,
      ...config.params,
    };
    return config;
  },

  /**
   * @description: 响应拦截器处理
   */
  responseInterceptors: (res: AxiosResponse<any>) => {
    return res;
  },

  /**
   * @description: 响应错误处理
   */
  responseInterceptorsCatch: (axiosInstance: AxiosInstance, error: any) => {
    // const useErrorLogStoreWithOut = useStore('useErrorLogStoreWithOut');
    const errorLogStore = useErrorLogStoreWithOut();
    errorLogStore.addAjaxErrorInfo(error);
    const { response, code, message, config } = error || {};
    const errorMessageMode = config?.requestOptions?.errorMessageMode || 'none';
    const msg: string = response?.data?.error?.message ?? '';
    const err: string = error?.toString?.() ?? '';
    let errMessage = '';

    if (axios.isCancel(error)) {
      return Promise.reject(error);
    }

    if (response.data && response.data.code === 401 && response.config.url !== '/token/refresh') {
      return handleByRefreshStatus(response.config);
    }

    try {
      if (code === 'ECONNABORTED' && message.indexOf('timeout') !== -1) {
        errMessage = $t('sys.api.apiTimeoutMessage');
      }
      if (err?.includes('Network Error')) {
        errMessage = $t('sys.api.networkExceptionMsg');
      }

      if (errMessage) {
        if (errorMessageMode === 'modal') {
          Modal.error({ title: $t('sys.api.errorTip'), content: errMessage });
        } else if (errorMessageMode === 'message') {
          Message.error(errMessage);
        }
        return Promise.reject(error);
      }
    } catch (error) {
      throw new Error(error as unknown as string);
    }

    checkStatus(error?.response?.status, msg, errorMessageMode);

    // 添加自动重试机制 保险起见 只针对GET请求
    const retryRequest = new AxiosRetry();
    const { isOpenRetry } = config.requestOptions.retryRequest;
    config.method?.toUpperCase() === RequestEnum.GET &&
      isOpenRetry &&
      // @ts-ignore
      retryRequest.retry(axiosInstance, error);
    return Promise.reject(error);
  },
};

// 请求队列
const requestQueue: RequestQueue = {
  // 请求列表，存储刷新 token 期间发起的请求
  list: [],
  isRefreshing: false,
  // 添加请求
  add(config: any, resolve: (value: unknown) => void, reject: (reason: unknown) => void) {
    this.list?.push((newToken: string) => {
      if (newToken) {
        config.headers['Access-Token'] = newToken;
        resolve(axios(config));
      } else {
        // token不为真 表示 token 获取失败，清理队列请求的 Promise，避免内存堆积
        reject({ status: 200, data: { code: 401 }, message: 'token已失效,请重新登录' });
      }
    });
  },
  // 执行队列
  execute(token: string) {
    let fn;
    // eslint-disable-next-line
    while ((fn = this.list?.shift())) {
      fn(token);
    }
  },
  // 清空队列
  clear() {
    this.execute();
  },
};

function handleByRefreshStatus(config: any) {
  return new Promise((resolve, reject) => {
    const status = requestQueue.isRefreshing;
    requestQueue.add(config, resolve, reject);
    if (!status) {
      getNewToken();
    }
  });
}

async function getNewToken() {
  // 获取新token
  try {
    if (requestQueue.isRefreshing) {
      return false;
    }
    // const useUserStoreWithOut = useStore('useUserStoreWithOut');
    const userStore = useUserStoreWithOut();
    requestQueue.isRefreshing = true;
    const data = await userStore.loadRefreshToken();
    requestQueue.isRefreshing = false;
    requestQueue.execute(data?.token);
  } catch (error) {
    requestQueue.isRefreshing = false;
    requestQueue.clear();
    loginExpired();
  }
}

// 登录过期弹窗标记
let isLoginExpiredModalShown = false;

function loginExpired() {
  // 如果已经显示过弹窗,则不再重复显示
  if (isLoginExpiredModalShown) {
    return;
  }

  setTimeout(() => {
    const path = window.document.location.hash;
    if (path !== '#/' && path.indexOf('/login') === -1) {
      isLoginExpiredModalShown = true;
      Modal.error({
        title: '登录已过期',
        content: '很抱歉，登录已过期，请重新登录',
        okText: '重新登录',
        mask: false,
        onOk: () => {
          isLoginExpiredModalShown = false;
          // const useUserStoreWithOut = useStore('useUserStoreWithOut');
          const userStore = useUserStoreWithOut();
          userStore.logout(true);
        },
        onCancel: () => {
          isLoginExpiredModalShown = false;
        },
      });
    }
  }, 200);
}

function createAxios(opt?: Partial<CreateAxiosOptions>) {
  // const { env } = useBridge();
  const env = import.meta.env;
  const { VITE_GLOB_API_URL_PREFIX: urlPrefix, VITE_GLOB_API_URL: apiUrl } = env;
  return new VAxios(
    // 深度合并
    deepMerge(
      {
        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication#authentication_schemes
        // authentication schemes，e.g: Bearer
        // authenticationScheme: 'Bearer',
        authenticationScheme: '',
        timeout: 10 * 1000,
        // 基础接口地址
        baseURL: apiUrl,

        headers: { 'Content-Type': ContentTypeEnum.JSON },
        // 如果是form-data格式
        // headers: { 'Content-Type': ContentTypeEnum.FORM_URLENCODED },
        // 数据处理方式
        transform: clone(transform),
        // 配置项，下面的选项都可以在独立的接口请求中覆盖
        requestOptions: {
          // 默认将prefix 添加到url
          joinPrefix: true,
          // 是否返回原生响应头 比如：需要获取响应头时使用该属性
          isReturnNativeResponse: false,
          // 需要对返回数据进行处理
          isTransformResponse: true,
          // post请求的时候添加参数到url
          joinParamsToUrl: false,
          // 格式化提交参数时间
          formatDate: true,
          // 消息提示类型
          errorMessageMode: 'message',
          // 接口地址
          apiUrl: apiUrl,
          // 接口拼接地址
          urlPrefix: urlPrefix,
          //  是否加入时间戳
          joinTime: true,
          // 忽略重复请求
          ignoreCancelToken: true,
          // 是否携带token
          withToken: true,
          retryRequest: {
            isOpenRetry: true,
            count: 5,
            waitTime: 100,
          },
        },
      },
      opt || {},
    ),
  );
}
export const defHttp = createAxios();

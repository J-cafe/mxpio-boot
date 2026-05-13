import type { AxiosRequestConfig } from 'axios';

export type ErrorMessageMode = 'none' | 'modal' | 'message' | undefined;
export type SuccessMessageMode = ErrorMessageMode;

export interface RequestOptions {
  // Splicing request parameters to url
  joinParamsToUrl?: boolean;
  // Format request parameter time
  formatDate?: boolean;
  // Whether to process the request result
  isTransformResponse?: boolean;
  // Whether to return native response headers
  // For example: use this attribute when you need to get the response headers
  isReturnNativeResponse?: boolean;
  // Whether to join url
  joinPrefix?: boolean;
  // Interface address, use the default apiUrl if you leave it blank
  apiUrl?: string;
  // 请求拼接路径
  urlPrefix?: string;
  // Error message prompt type
  errorMessageMode?: ErrorMessageMode;
  // Success message prompt type
  successMessageMode?: SuccessMessageMode;
  // Whether to add a timestamp
  joinTime?: boolean;
  ignoreCancelToken?: boolean;
  // Whether to send token in header
  withToken?: boolean;
  // 请求重试机制
  retryRequest?: RetryRequest;
}

export interface RetryRequest {
  isOpenRetry: boolean;
  count: number;
  waitTime: number;
}
export interface Result<T = any> {
  code: number | string;
  type: 'success' | 'error' | 'warning';
  message: string;
  result: T;
}

// multipart/form-data: upload file
export interface UploadFileParams {
  // Other parameters
  data?: Recordable;
  // File parameter interface field name
  name?: string;
  // file name
  file: File | Blob;
  // file name
  filename?: string;
  [key: string]: any;
}

export interface RequestQueue {
  //待重新执行方法
  list?: Function[];
  //是否正在刷新
  isRefreshing?: Boolean;
  // 将方法添加进待执行队列
  add: Function;
  // 执行队列中的方法
  execute: Function;
  // 移除队列中的方法
  clear: Function;
}

// 请求查询构造器
export interface RequestCriteriaParams {
  pageNo?: string | number;
  pageSize?: string | number;
  criteria?: string;
  [key: string]: any;
}

export interface VAxiosOptions {
  uploadFile: <T = any>(config: AxiosRequestConfig, params: UploadFileParams) => Promise<T>;
  get: <T = any>(config: AxiosRequestConfig, options?: RequestOptions) => Promise<T>;
  post: <T = any>(config: AxiosRequestConfig, options?: RequestOptions) => Promise<T>;
  put: <T = any>(config: AxiosRequestConfig, options?: RequestOptions) => Promise<T>;
  delete: <T = any>(config: AxiosRequestConfig, options?: RequestOptions) => Promise<T>;
  request: <T = any>(config: AxiosRequestConfig, options?: RequestOptions) => Promise<T>;
  [key: string]: any;
}

export type * from 'axios';

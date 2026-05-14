import type { SSOSettingOptions } from './types';

export const ssoSettingDef: SSOSettingOptions = {
  enableSSO: undefined,
  ssoLoginUrl: '', // 统一认证接口地址 示例https://login.microsoftonline.com/common
  ssoClientId: '', // 统一认证clientId
};

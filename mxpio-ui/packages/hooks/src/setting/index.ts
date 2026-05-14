// import type { GlobConfig } from '@mxpio/types';
// import { getAppEnvConfig } from '@mxpio/utils';

// 已废弃useGlobSetting配置,改为项目env内获取
// export const useGlobSetting = (): Readonly<GlobConfig> => {
//   const {
//     VITE_GLOB_APP_TITLE,
//     VITE_GLOB_API_URL,
//     VITE_GLOB_API_URL_PREFIX,
//     VITE_GLOB_UPLOAD_URL,
//     VITE_GLOB_APP_SSO,
//   } = getAppEnvConfig();

//   // Take global configuration
//   const glob: Readonly<GlobConfig> = {
//     title: VITE_GLOB_APP_TITLE,
//     apiUrl: VITE_GLOB_API_URL,
//     shortName: VITE_GLOB_APP_TITLE.replace(/\s/g, '_').replace(/-/g, '_'),
//     urlPrefix: VITE_GLOB_API_URL_PREFIX,
//     uploadUrl: VITE_GLOB_UPLOAD_URL,
//     enableSSO: VITE_GLOB_APP_SSO, // 开启统一认证，feishu | wechat | dingtalk | oauth | MSAL，为空时：采用本地账号密码认证
//   };
//   return glob as Readonly<GlobConfig>;
// };

export * from './useDarkModeTheme';
export * from './useHeaderSetting';
export * from './useMenuSetting';
export * from './useRootSetting';
export * from './useTransitionSetting';

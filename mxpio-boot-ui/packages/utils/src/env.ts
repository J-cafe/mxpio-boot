import { API_ADDRESS } from '@mxpio/enums';
import type { GlobEnvConfig } from '@mxpio/types';
import pkg from '../package.json';

const env = import.meta.env;

export function getCommonStoragePrefix() {
  // const { VITE_GLOB_APP_TITLE } = getAppEnvConfig();
  const { VITE_GLOB_APP_TITLE } = env;
  return `${VITE_GLOB_APP_TITLE.replace(/\s/g, '_')}__${getEnv()}`.toUpperCase();
}

// Generate cache key according to version
export function getStorageShortName() {
  return `${getCommonStoragePrefix()}${`__${pkg.version}`}__`.toUpperCase();
}

const getVariableName = (title: string) => {
  function strToHex(str: string) {
    const result: string[] = [];
    for (let i = 0; i < str.length; ++i) {
      const hex = str.charCodeAt(i).toString(16);
      result.push(('000' + hex).slice(-4));
    }
    return result.join('').toUpperCase();
  }
  return `__PRODUCTION__${strToHex(title) || '__APP'}__CONF__`.toUpperCase().replace(/\s/g, '');
};

// 已废弃，移除appconfig全局配置，使用项目env变量
export function getAppEnvConfig() {
  // const env = common.getEnv();
  const ENV_NAME: string = getVariableName(env.VITE_GLOB_APP_TITLE);
  const ENV = env.DEV
    ? // Get the global configuration (the configuration will be extracted independently when packaging)
      (env as unknown as GlobEnvConfig)
    : ((window as any)[ENV_NAME] as unknown as GlobEnvConfig);

  const { VITE_GLOB_APP_TITLE, VITE_GLOB_API_URL_PREFIX, VITE_GLOB_UPLOAD_URL, VITE_GLOB_APP_SSO } =
    ENV;
  let { VITE_GLOB_API_URL } = ENV;
  if (localStorage.getItem(API_ADDRESS)) {
    const address = JSON.parse(localStorage.getItem(API_ADDRESS) || '{}');
    if (address?.key) VITE_GLOB_API_URL = address?.val;
  }
  return {
    VITE_GLOB_APP_TITLE,
    VITE_GLOB_API_URL,
    VITE_GLOB_API_URL_PREFIX,
    VITE_GLOB_UPLOAD_URL,
    VITE_GLOB_APP_SSO,
  };
}

/**
 * @description: Development mode
 */
export const devMode = 'development';

/**
 * @description: Production mode
 */
export const prodMode = 'production';

/**
 * @description: Get environment variables
 * @returns:
 * @example:
 */
export function getEnv(): string {
  return env.MODE;
}

/**
 * @description: Is it a development mode
 * @returns:
 * @example:
 */
export function isDevMode(): boolean {
  return env.DEV;
}

/**
 * @description: Is it a production mode
 * @returns:
 * @example:
 */
export function isProdMode(): boolean {
  return env.PROD;
}

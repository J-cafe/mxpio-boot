import { CacheTypeEnum, TOKEN_KEY } from '@mxpio/enums';
// import { projectSetting } from '@mxpio/settings';
import setting from '@mxpio/settings';

import { BasicKeys, Persistent } from '../cache/persistent';

// const { permissionCacheType } = projectSetting;
// const isLocal = permissionCacheType === CacheTypeEnum.LOCAL;

function isLocal() {
  const { permissionCacheType } = setting.getProjectSetting();
  const isLocal = permissionCacheType === CacheTypeEnum.LOCAL;
  return isLocal;
}

export function getToken() {
  return getAuthCache(TOKEN_KEY);
}

export function getAuthCache<T>(key: BasicKeys) {
  const fn = isLocal() ? Persistent.getLocal : Persistent.getSession;
  return fn(key) as T;
}

export function setAuthCache(key: BasicKeys, value: any) {
  const fn = isLocal() ? Persistent.setLocal : Persistent.setSession;
  return fn(key, value, true);
}

export function clearAuthCache(immediate = true) {
  const fn = isLocal() ? Persistent.clearLocal : Persistent.clearSession;
  return fn(immediate);
}

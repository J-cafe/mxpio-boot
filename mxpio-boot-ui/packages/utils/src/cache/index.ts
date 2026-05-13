// import { DEFAULT_CACHE_TIME, SHOULD_ENABLE_STORAGE_ENCRYPTION } from '@mxpio/settings';
import setting from '@mxpio/settings';

import { getStorageShortName } from '../env';
import { createStorage as create, CreateStorageParams } from './storageCache';

export type Options = Partial<CreateStorageParams>;

const createOptions = (storage: Storage, options: Options = {}): Options => {
  const encryptionSetting = setting.getEncryptionSetting();
  return {
    // No encryption in debug mode
    hasEncrypt: encryptionSetting.SHOULD_ENABLE_STORAGE_ENCRYPTION,
    storage,
    prefixKey: getStorageShortName(),
    ...options,
  };
};

export const WebStorage = create(createOptions(sessionStorage));

export const createStorage = (storage: Storage = sessionStorage, options: Options = {}) => {
  return create(createOptions(storage, options));
};

export const createSessionStorage = (options: Options = {}) => {
  const encryptionSetting = setting.getEncryptionSetting();
  return createStorage(sessionStorage, {
    ...options,
    timeout: encryptionSetting.DEFAULT_CACHE_TIME,
  });
};

export const createLocalStorage = (options: Options = {}) => {
  const encryptionSetting = setting.getEncryptionSetting();
  return createStorage(localStorage, { ...options, timeout: encryptionSetting.DEFAULT_CACHE_TIME });
};

export default WebStorage;
export * from './persistent';

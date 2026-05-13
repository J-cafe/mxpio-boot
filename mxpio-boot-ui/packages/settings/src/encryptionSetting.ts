import type { EncryptionSettingOptions } from './types';

export const encryptionSettingDef: EncryptionSettingOptions = {
  DEFAULT_CACHE_TIME: 60 * 60 * 24 * 7,
  cacheCipher: {
    key: '_11111000001111@',
    iv: '@11111000001111_',
  },
  SHOULD_ENABLE_STORAGE_ENCRYPTION: !!import.meta.env.DEV,
};

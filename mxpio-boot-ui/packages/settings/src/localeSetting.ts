import type { LocaleSettingOptions } from './types';

export const localeSettingDef: LocaleSettingOptions = {
  LOCALE: {
    ZH_CN: 'zh_CN',
    EN_US: 'en',
  },
  localeSetting: {
    showPicker: true,
    locale: 'zh_CN',
    fallback: 'zh_CN',
    availableLocales: ['zh_CN', 'en'],
  },
  localeList: [
    {
      text: '简体中文',
      event: 'zh_CN',
    },
    {
      text: 'English',
      event: 'en',
    },
  ],
};

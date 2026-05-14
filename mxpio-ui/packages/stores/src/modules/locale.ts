import type { LocaleSetting, LocaleType } from '@mxpio/types';

import { defineStore } from 'pinia';
import { store } from '../pinia';

import { LOCALE_KEY } from '@mxpio/enums';
import { createLocalStorage } from '@mxpio/utils';
// import { localeSetting } from '@mxpio/settings';
import setting from '@mxpio/settings';

const ls = createLocalStorage();
const locale = setting.getLocaleSetting();
const lsLocaleSetting = (ls.get(LOCALE_KEY) || locale.localeSetting) as LocaleSetting;

export interface LocaleState {
  localInfo: LocaleSetting;
}

export const useLocaleStore = defineStore({
  id: 'app-locale',
  state: (): LocaleState => ({
    localInfo: lsLocaleSetting,
  }),
  getters: {
    getShowPicker(state): boolean {
      return !!state.localInfo?.showPicker;
    },
    getLocale(state): LocaleType {
      return state.localInfo?.locale ?? 'zh_CN';
    },
  },
  actions: {
    /**
     * Set up multilingual information and cache
     * @param info multilingual info
     */
    setLocaleInfo(info: Partial<LocaleSetting>) {
      this.localInfo = { ...this.localInfo, ...info };
      ls.set(LOCALE_KEY, this.localInfo);
    },
    /**
     * Initialize multilingual information and load the existing configuration from the local cache
     */
    initLocale() {
      this.setLocaleInfo({
        ...locale.localeSetting,
        ...this.localInfo,
      });
    },
  },
});

// Need to be used outside the setup
export function useLocaleStoreWithOut() {
  return useLocaleStore(store);
}

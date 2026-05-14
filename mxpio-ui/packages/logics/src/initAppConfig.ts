/**
 * Application configuration
 */
import type { ProjectConfig } from '@mxpio/types';

import { ThemeEnum } from '@mxpio/enums';
// import { projectSetting } from '@mxpio/settings/src/projectSetting';

import { updateDarkTheme } from './theme/dark';
import { updateHeaderBgColor, updateSidebarBgColor } from './theme/updateBackground';
import { updateColorWeak } from './theme/updateColorWeak';
import { updateGrayMode } from './theme/updateGrayMode';

import { useAppStore, useLocaleStore } from '@mxpio/stores';
import setting from '@mxpio/settings';

import { getCommonStoragePrefix, getStorageShortName } from '@mxpio/utils/src/env';

// import { deepMerge } from '@mxpio/utils';
// import { Persistent } from '@mxpio/utils/src/cache/persistent';

// Initial project configuration
export function initAppConfigStore() {
  // 获取项目配置
  const projectSetting_ = setting.getProjectSetting();
  const localeStore = useLocaleStore();
  const appStore = useAppStore();
  // let projCfg: ProjectConfig = Persistent.getLocal(PROJ_CFG_KEY) as ProjectConfig;
  // projCfg = deepMerge(projectSetting_, projCfg);

  // projCfg = deepMerge(projectSetting, Object.assign({}, projectSetting_, projCfg));
  const projCfg: ProjectConfig = projectSetting_;
  const darkMode = appStore.getDarkMode;
  const {
    colorWeak,
    grayMode,
    headerSetting: { bgColor: headerBgColor } = {},
    menuSetting: { bgColor } = {},
  } = projCfg;
  try {
    grayMode && updateGrayMode(grayMode);
    colorWeak && updateColorWeak(colorWeak);
  } catch (error) {
    console.log(error);
  }
  // appStore.setProjectConfig(projCfg);

  // init dark mode
  updateDarkTheme(darkMode);
  if (darkMode === ThemeEnum.DARK) {
    updateHeaderBgColor();
    updateSidebarBgColor();
  } else {
    headerBgColor && updateHeaderBgColor(headerBgColor);
    bgColor && updateSidebarBgColor(bgColor);
  }
  // init store
  localeStore.initLocale();

  setTimeout(() => {
    clearObsoleteStorage();
  }, 16);
}

/**
 * As the version continues to iterate, there will be more and more cache keys stored in localStorage.
 * This method is used to delete useless keys
 */
export function clearObsoleteStorage() {
  const commonPrefix = getCommonStoragePrefix();
  const shortPrefix = getStorageShortName();

  [localStorage, sessionStorage].forEach((item: Storage) => {
    Object.keys(item).forEach((key) => {
      if (key && key.startsWith(commonPrefix) && !key.startsWith(shortPrefix)) {
        item.removeItem(key);
      }
    });
  });
}

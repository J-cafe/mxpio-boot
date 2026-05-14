import type {
  ProjectConfig,
  HeaderSetting,
  MenuSetting,
  TransitionSetting,
  MultiTabsSetting,
  BeforeMiniState,
} from '@mxpio/types';
import { defineStore } from 'pinia';
import { store } from '../pinia';
import { ThemeEnum, APP_DARK_MODE_KEY, PROJ_CFG_KEY } from '@mxpio/enums';
import { Persistent, deepMerge } from '@mxpio/utils';
import { resetRouter } from '@mxpio/router-config';
import setting from '@mxpio/settings';
// import { darkMode } from '@mxpio/settings';
// import { useUserStore } from './user';
// import { useProfile } from '../hooks/useProfile';
// const { saveProfile, getProfile } = useProfile();
// const projectSetting = setting.getProjectSetting();

export interface AppState {
  darkMode?: ThemeEnum;
  // Page loading status
  pageLoading: boolean;
  // project config
  projectConfig: ProjectConfig | null;
  // When the window shrinks, remember some states, and restore these states when the window is restored
  beforeMiniInfo: BeforeMiniState;
}

let timeId: TimeoutHandle;
export const useAppStore = defineStore({
  id: 'app',
  state: (): AppState => ({
    darkMode: undefined,
    pageLoading: false,
    projectConfig: Persistent.getLocal(PROJ_CFG_KEY),
    beforeMiniInfo: {},
  }),
  getters: {
    getPageLoading(state): boolean {
      return state.pageLoading;
    },
    getDarkMode(state): 'light' | 'dark' | string {
      const designSetting = setting.getDesignSetting();
      return (
        state.darkMode ||
        localStorage.getItem(APP_DARK_MODE_KEY) ||
        (designSetting.darkMode as string)
      );
    },

    getBeforeMiniInfo(state): BeforeMiniState {
      return state.beforeMiniInfo;
    },

    getProjectConfig(state): ProjectConfig {
      const projectSetting = setting.getProjectSetting();
      return deepMerge(projectSetting, state.projectConfig || {});
      // return state.projectConfig || ({} as ProjectConfig);
    },

    getHeaderSetting(): HeaderSetting {
      return this.getProjectConfig.headerSetting;
    },
    getMenuSetting(): MenuSetting {
      return this.getProjectConfig.menuSetting;
    },
    getTransitionSetting(): TransitionSetting {
      return this.getProjectConfig.transitionSetting;
    },
    getMultiTabsSetting(): MultiTabsSetting {
      return this.getProjectConfig.multiTabsSetting;
    },
    // getApiAddress() {
    //   return JSON.parse(localStorage.getItem(API_ADDRESS) || '{}');
    // },
  },
  actions: {
    setPageLoading(loading: boolean): void {
      this.pageLoading = loading;
    },

    setDarkMode(mode: ThemeEnum): void {
      this.darkMode = mode;
      localStorage.setItem(APP_DARK_MODE_KEY, mode);
    },

    setBeforeMiniInfo(state: BeforeMiniState): void {
      this.beforeMiniInfo = state;
    },

    async setProjectConfig(config: DeepPartial<ProjectConfig>): Promise<void> {
      this.projectConfig = deepMerge(this.projectConfig || {}, config) as ProjectConfig;
      Persistent.setLocal(PROJ_CFG_KEY, this.projectConfig);
      // 后续实现配置后台存储
      // const useUser = useUserStore();
      // const res = await saveProfile({
      //   userId: useUser.userInfo?.username as string,
      //   pageKey: '/',
      //   elementKey: 'system',
      //   properties: JSON.stringify(this.projectConfig),
      // });
    },
    setMenuSetting(setting: Partial<MenuSetting>): void {
      this.projectConfig!.menuSetting = deepMerge(this.projectConfig!.menuSetting, setting);
      Persistent.setLocal(PROJ_CFG_KEY, this.projectConfig);
    },
    async resetAllState() {
      resetRouter();
      Persistent.clearAll();
    },
    async setPageLoadingAction(loading: boolean): Promise<void> {
      if (loading) {
        clearTimeout(timeId);
        // Prevent flicker
        timeId = setTimeout(() => {
          this.setPageLoading(loading);
        }, 50);
      } else {
        this.setPageLoading(loading);
        clearTimeout(timeId);
      }
    },
    // setApiAddress(config: ApiAddress): void {
    //   localStorage.setItem(API_ADDRESS, JSON.stringify(config));
    // },
    async getProjectConfigAction() {
      // 后续实现配置后台存储
      // const useUser = useUserStore();
      // const config = await getProfile({
      //   userId: useUser.userInfo?.username as string,
      //   pageKey: '/',
      //   elementKey: 'system',
      // });
      // this.projectConfig = deepMerge(this.projectConfig || {}, config) as ProjectConfig;
    },
  },
});

// Need to be used outside the setup
export function useAppStoreWithOut() {
  return useAppStore(store);
}

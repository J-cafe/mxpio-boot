import type { UserInfo, ErrorMessageMode } from '@mxpio/types';
import { defineStore } from 'pinia';
import { store } from '../pinia';
import {
  RoleEnum,
  PageEnum,
  ROLES_KEY,
  TOKEN_KEY,
  USER_INFO_KEY,
  REFRESH_TOKEN_KEY,
} from '@mxpio/enums';
import { getAuthCache, setAuthCache, isArray, fileToBase64 } from '@mxpio/utils';
import type { GetUserInfoModel, LoginParams, RefreshTokenResult } from '@mxpio/api';
import { doLogout, getUserInfo, loginApi, refreshToken, downloadApi } from '@mxpio/api';
import { $t } from '@mxpio/locales';
import { Modal } from 'ant-design-vue';
import { useBridge } from '@mxpio/bridge';
import { usePermissionStore } from './permission';
import { h } from 'vue';

export interface UserState {
  userInfo: Nullable<UserInfo>;
  token?: string;
  roleList: RoleEnum[];
  sessionTimeout?: boolean;
  lastUpdateTime: number;
  refreshToken?: string;
}

export const useUserStore = defineStore({
  id: 'app-user',
  state: (): UserState => ({
    // user info
    userInfo: null,
    // token
    token: undefined,
    refreshToken: undefined,
    // roleList
    roleList: [],
    // Whether the login expired
    sessionTimeout: false,
    // Last fetch time
    lastUpdateTime: 0,
  }),
  getters: {
    getUserInfo(state): UserInfo {
      return state.userInfo || getAuthCache<UserInfo>(USER_INFO_KEY) || {};
    },
    getToken(state): string {
      return state.token || getAuthCache<string>(TOKEN_KEY);
    },
    getRoleList(state): RoleEnum[] {
      return state.roleList.length > 0 ? state.roleList : getAuthCache<RoleEnum[]>(ROLES_KEY);
    },
    getSessionTimeout(state): boolean {
      return !!state.sessionTimeout;
    },
    getLastUpdateTime(state): number {
      return state.lastUpdateTime;
    },
  },
  actions: {
    setToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(TOKEN_KEY, info);
    },
    setRefreshToken(info: string | undefined) {
      this.token = info ? info : ''; // for null or undefined value
      setAuthCache(REFRESH_TOKEN_KEY, info);
    },
    setRoleList(roleList: RoleEnum[]) {
      this.roleList = roleList;
      setAuthCache(ROLES_KEY, roleList);
    },
    setUserInfo(info: UserInfo | null) {
      this.userInfo = info;
      this.lastUpdateTime = new Date().getTime();
      setAuthCache(USER_INFO_KEY, info);
    },
    setSessionTimeout(flag: boolean) {
      this.sessionTimeout = flag;
    },
    setAvatarImg(avatarImage: string | undefined) {
      if (this.userInfo) {
        this.userInfo.avatarImage = avatarImage;
      }
    },
    resetState() {
      this.userInfo = null;
      this.token = '';
      this.roleList = [];
      this.sessionTimeout = false;
    },
    /**
     * @description: login
     */
    async login(
      params: LoginParams & {
        goHome?: boolean;
        mode?: ErrorMessageMode;
      },
    ): Promise<GetUserInfoModel | null> {
      try {
        const { goHome = true, mode, ...loginParams } = params;
        const data = await loginApi(loginParams, mode);
        const { token, refreshToken } = data;

        // save token
        this.setToken(token);
        this.setRefreshToken(refreshToken);
        return this.afterLoginAction(goHome);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async afterLoginAction(goHome?: boolean): Promise<GetUserInfoModel | null> {
      if (!this.getToken) return null;
      // get user info
      const userInfo = await this.getUserInfoAction();

      const sessionTimeout = this.sessionTimeout;
      if (sessionTimeout) {
        this.setSessionTimeout(false);
      } else {
        // 密码过期
        if (userInfo?.pwdExpiredFlag) {
          return userInfo;
        }
        const permissionStore = usePermissionStore();
        const { router } = useBridge();
        if (!permissionStore.isDynamicAddedRoute) {
          await permissionStore.addRoutesAction();
          permissionStore.setDynamicAddedRoute(true);
        }
        goHome && (await router.replace(userInfo?.homePath || PageEnum.BASE_HOME));
      }
      return userInfo;
    },
    async getUserInfoAction(): Promise<UserInfo | null> {
      if (!this.getToken) return null;
      const userInfo = await getUserInfo();
      const { authorities = [] } = userInfo;
      if (isArray(authorities)) {
        const roleList = authorities.map((item) => item.roleId) as RoleEnum[];
        this.setRoleList(roleList);
      } else {
        userInfo.roles = [];
        this.setRoleList([]);
      }
      // 兼容框架原有realName字段
      userInfo.realName = userInfo.nickname;

      // 兼容框架原有roles字段
      userInfo.roles = userInfo.authorities;

      this.setUserInfo(userInfo);
      if (userInfo.avatar) {
        this.getAvatarImg(userInfo.avatar);
      }
      return userInfo;
    },
    /**
     * @description: logout
     */
    async logout(goLogin = false) {
      if (this.getToken) {
        try {
          await doLogout();
        } catch {
          console.log('注销Token失败');
        }
      }
      this.setToken(undefined);
      this.setRefreshToken(undefined);
      this.setSessionTimeout(false);
      this.setUserInfo(null);
      const { router } = useBridge();
      goLogin && router.push(PageEnum.BASE_LOGIN);
    },

    /**
     * @description: Confirm before logging out
     */
    confirmLoginOut() {
      // const { createConfirm } = useMessage();
      // createConfirm({
      //   iconType: 'warning',
      //   title: () => h('span', $t('sys.app.logoutTip')),
      //   content: () => h('span', $t('sys.app.logoutMessage')),
      //   onOk: async () => {
      //     await this.logout(true);
      //   },
      // });
      Modal.confirm({
        iconType: 'warning',
        title: () => h('span', $t('sys.app.logoutTip')),
        content: () => h('span', $t('sys.app.logoutMessage')),
        onOk: async () => {
          await this.logout(true);
        },
      });
    },
    /**
     * @description: 刷新token
     */
    async loadRefreshToken(): Promise<RefreshTokenResult | null> {
      const res = await refreshToken({
        refreshToken: this.refreshToken || getAuthCache<string>(REFRESH_TOKEN_KEY),
        token: this.token || getAuthCache<string>(TOKEN_KEY),
      });
      this.setToken(res.token);
      this.setRefreshToken(res.refreshToken);
      return res;
    },
    async getAvatarImg(fileNo: string): Promise<undefined> {
      //获取头像base64
      const { data } = await downloadApi(fileNo);
      const { result } = await fileToBase64(data);
      this.setAvatarImg(result);
    },
  },
});

// Need to be used outside the setup
export function useUserStoreWithOut() {
  return useUserStore(store);
}

import { defineStore } from 'pinia';
import { getSystemConfig } from '@mxpio/api';
import { store } from '../pinia';

interface PasswordStrategy {
  // 正则表达式
  pattern: string;
  // 不通过提示信息
  message: string;
}

export interface SystemState {
  // 是否显示验证码
  captchaOpenFlag: boolean;
  // 密码校验规则
  passwordStrategy: PasswordStrategy[];
  // 应用名称
  appName: string;
  // 应用简称
  appSystemAbbr: string;
  // 应用描述
  appSystemDesc: string;
  // 应用公司
  appUserCompany: string;
  // 应用logo
  logo?: string;
}

export const useSystemStore = defineStore({
  id: 'system',
  state: (): SystemState => ({
    captchaOpenFlag: false,
    passwordStrategy: [],
    appName: '',
    appSystemAbbr: '',
    appSystemDesc: '',
    appUserCompany: '',
    logo: '',
  }),
  actions: {
    setSystem(data: SystemState) {
      this.captchaOpenFlag = data.captchaOpenFlag;
      this.passwordStrategy = data.passwordStrategy;
      this.appName = data.appName;
      this.appSystemAbbr = data.appSystemAbbr;
      this.appSystemDesc = data.appSystemDesc;
      this.appUserCompany = data.appUserCompany;
      this.logo = data.logo;
    },
    async getSystemAction(): Promise<SystemState> {
      try {
        const res = await getSystemConfig();
        this.setSystem(res);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
  },
});

export function useSystemStoreWithOut() {
  return useSystemStore(store);
}

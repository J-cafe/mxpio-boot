import { siteSettingDef } from './siteSetting';
import { localeSettingDef } from './localeSetting';
import { encryptionSettingDef } from './encryptionSetting';
import { designSettingDef } from './designSetting';
import { componentSettingDef } from './componentSetting';
import { ssoSettingDef } from './ssoSetting';
import { projectSetting as projectSettingDef } from './projectSetting';
import type { ProjectConfig } from '@mxpio/types';
import { merge } from 'lodash-es';

import type {
  DesignSettingOptions,
  EncryptionSettingOptions,
  LocaleSettingOptions,
  SiteSettingOptions,
  SSOSettingOptions,
  SettingOptions,
  ComponentSettingOptions,
  FullSettingOptions,
} from './types';

export * from './componentSetting';
export * from './designSetting';
export * from './encryptionSetting';
export * from './localeSetting';
export * from './projectSetting';
export * from './siteSetting';
export * from './types';

/**
 * Setting 类 - 全局配置管理
 * 提供类型安全的配置获取和设置方法
 */
class Setting {
  // 使用 readonly 防止默认值被意外修改
  private readonly defaultSettings: FullSettingOptions = {
    siteSetting: siteSettingDef,
    localeSetting: localeSettingDef,
    encryptionSetting: encryptionSettingDef,
    designSetting: designSettingDef,
    componentSetting: componentSettingDef,
    ssoSetting: ssoSettingDef,
    projectSetting: projectSettingDef,
  };

  // 当前配置，使用 readonly 确保键不变
  private settings: FullSettingOptions = { ...this.defaultSettings };

  /**
   * 深度合并配置
   * @param key 配置键
   * @param config 要合并的部分配置
   */
  private mergeConfig<K extends keyof FullSettingOptions>(
    key: K,
    config: Partial<FullSettingOptions[K]>,
  ): void {
    this.settings[key] = merge({}, this.defaultSettings[key], config);
  }

  /**
   * 获取指定配置
   * @param key 配置键
   * @returns 配置对象
   */
  private getConfig<K extends keyof FullSettingOptions>(key: K): FullSettingOptions[K] {
    return this.settings[key];
  }

  /**
   * 获取站点设置
   */
  getSiteSetting(): SiteSettingOptions {
    return this.getConfig('siteSetting');
  }

  /**
   * 获取语言设置
   */
  getLocaleSetting(): LocaleSettingOptions {
    return this.getConfig('localeSetting');
  }

  /**
   * 获取加密设置
   */
  getEncryptionSetting(): EncryptionSettingOptions {
    return this.getConfig('encryptionSetting');
  }

  /**
   * 获取设计设置
   */
  getDesignSetting(): DesignSettingOptions {
    return this.getConfig('designSetting');
  }

  /**
   * 获取组件设置
   */
  getComponentSetting(): ComponentSettingOptions {
    return this.getConfig('componentSetting');
  }

  /**
   * 获取SSO设置
   */
  getSSOSetting(): SSOSettingOptions {
    return this.getConfig('ssoSetting');
  }

  /**
   * 获取项目配置
   */
  getProjectSetting(): ProjectConfig {
    return this.getConfig('projectSetting');
  }

  /**
   * 获取所有配置
   */
  getAllSettings(): FullSettingOptions {
    return { ...this.settings };
  }

  /**
   * 批量设置配置
   * @param options 配置选项对象
   */
  setSetting(options: SettingOptions): void {
    if (!options) return;

    // 遍历传入的配置项并合并
    (Object.keys(this.defaultSettings) as Array<keyof FullSettingOptions>).forEach((key) => {
      const value = options[key];
      if (value && Object.keys(value).length > 0) {
        this.mergeConfig(key, value);
      }
    });
  }

  /**
   * 重置为默认配置
   */
  reset(): void {
    this.settings = { ...this.defaultSettings };
  }

  /**
   * 获取默认配置
   */
  getDefaultSettings(): FullSettingOptions {
    return { ...this.defaultSettings };
  }
}

const setting = new Setting();

/**
 * 注册项目配置
 * @param options 配置选项
 */
export function registerSetting(options: SettingOptions): void {
  setting.setSetting(options);
}

export default setting;

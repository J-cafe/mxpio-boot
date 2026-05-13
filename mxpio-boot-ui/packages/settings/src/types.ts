import { ThemeEnum } from '@mxpio/enums';
import type { LocaleSetting, LocaleType, ProjectConfig } from '@mxpio/types';

export interface DesignSettingOptions {
  prefixCls: string;
  multipleTabHeight: number;
  darkMode: ThemeEnum;
  footerHeight: number;
  layoutMultipleHeadePlaceholderTime: number;
  APP_PRESET_COLOR_LIST: string[];
  HEADER_PRESET_BG_COLOR_LIST: string[];
  SIDE_BAR_BG_COLOR_LIST: string[];
}

export interface EncryptionSettingOptions {
  DEFAULT_CACHE_TIME: number;
  cacheCipher: {
    key: string;
    iv: string;
  };
  SHOULD_ENABLE_STORAGE_ENCRYPTION: boolean;
}

export interface LocaleSettingOptions {
  LOCALE: { [key: string]: LocaleType };
  localeSetting: LocaleSetting;
  localeList: { text: string; event: string }[];
}

export interface SiteSettingOptions {
  GITHUB_URL: string;
  DOC_URL: string;
  SITE_URL: string;
}

export interface SSOSettingOptions {
  enableSSO?: string;
  ssoLoginUrl: string;
  ssoClientId: string;
}

export interface ComponentSettingOptions {
  table: {
    fetchSetting: {
      pageField: string;
      sizeField: string;
      listField: string;
      totalField: string;
    };
    pageSizeOptions: string[];
    defaultPageSize: number;
    defaultSize: string;
    defaultSortFn: (sortInfo: any) => any;
    defaultFilterFn: (data: any) => any;
  };
  vxeTable: {
    table: Record<string, any>;
    grid: Record<string, any>;
  };
  scrollbar: {
    native: boolean;
  };
}

// 定义所有设置项的键
export type SettingKey = keyof SettingOptions;

export interface SettingOptions {
  siteSetting?: Partial<SiteSettingOptions>;
  localeSetting?: Partial<LocaleSettingOptions>;
  encryptionSetting?: Partial<EncryptionSettingOptions>;
  designSetting?: Partial<DesignSettingOptions>;
  componentSetting?: Partial<ComponentSettingOptions>;
  ssoSetting?: Partial<SSOSettingOptions>;
  projectSetting?: DeepPartial<ProjectConfig>;
}

// 获取完整配置的类型（所有必填）
export interface FullSettingOptions {
  siteSetting: SiteSettingOptions;
  localeSetting: LocaleSettingOptions;
  encryptionSetting: EncryptionSettingOptions;
  designSetting: DesignSettingOptions;
  componentSetting: ComponentSettingOptions;
  ssoSetting: SSOSettingOptions;
  projectSetting: ProjectConfig;
}

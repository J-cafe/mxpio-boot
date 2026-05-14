/**
 * @package @mxpio/settings
 * @description 全局配置管理模块
 *
 * 该模块提供类型安全的全局配置管理功能，包括：
 * - 站点配置 (SiteSetting)
 * - 国际化配置 (LocaleSetting)
 * - 加密配置 (EncryptionSetting)
 * - 设计配置 (DesignSetting)
 * - 组件配置 (ComponentSetting)
 * - SSO配置 (SSOSetting)
 * - 项目配置 (ProjectSetting)
 *
 * @example
 * ```ts
 * import setting from '@mxpio/settings';
 * import { registerSetting } from '@mxpio/settings';
 *
 * // 获取配置
 * const designConfig = setting.getDesignSetting();
 * const projectConfig = setting.getProjectSetting();
 *
 * // 注册自定义配置
 * registerSetting({
 *   designSetting: {
 *     darkMode: ThemeEnum.DARK
 *   }
 * });
 * ```
 */

import setting from './src/index';

// 重新导出所有类型和常量
export * from './src';

// 默认导出 Setting 实例
export default setting;

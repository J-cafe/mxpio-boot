import type { App } from 'vue';
import type { Pinia } from 'pinia';
import { createPinia } from 'pinia';
import { registerPiniaPersistPlugin } from './plugin/persist';

interface PiniaOptions {
  persist?: boolean; // 控制是否启用持久化插件
}

// 1. 导出 Pinia 实例创建函数（支持自定义配置）
export function createPiniaInstance(options?: PiniaOptions): Pinia {
  const pinia = createPinia();

  // 2. 插件注册逻辑内聚（可扩展为支持多插件配置）
  if (options?.persist !== false) {
    // 支持通过 options 禁用持久化插件
    registerPiniaPersistPlugin(pinia);
  }

  return pinia;
}

// 3. 默认实例
export const store = createPiniaInstance();

// 4. 增强 setupStore：支持传入自定义 Pinia 实例
export function setupStore(app: App<Element>, customPinia?: Pinia) {
  app.use(customPinia || store);
}

// 导出类型和创建函数，支持高级场景
export type { Pinia, PiniaOptions };

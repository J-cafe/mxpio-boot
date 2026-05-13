import type { App, InjectionKey } from 'vue';
import { inject, getCurrentInstance } from 'vue';
import type { BridgeDependencies } from './type';

function isInVueContext(): boolean {
  return getCurrentInstance() !== null;
}

export const FrameworkKey: InjectionKey<BridgeDependencies> = Symbol('FRAMEWORK_DEPS');
let globalDeps: BridgeDependencies | null;
export function useBridge() {
  try {
    if (isInVueContext()) {
      // 尝试在 Vue 上下文中获取
      const vueDeps = inject(FrameworkKey, null);
      if (vueDeps) return vueDeps;
    }
  } catch (e) {
    // 忽略 inject 在非上下文中的错误
  }

  // 回退到全局引用
  if (globalDeps) return globalDeps;

  throw new Error('Framework dependencies not initialized');
}

export function initBridge(app: App, options: BridgeDependencies) {
  // 冻结环境变量，防止运行时修改
  const frozenEnv = Object.freeze({ ...options.env });
  globalDeps = { ...options, env: frozenEnv };
  // configureContainer(globalDeps);
  app.provide(FrameworkKey, { ...options, env: frozenEnv });
}

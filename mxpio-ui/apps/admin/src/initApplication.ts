import { initBridge } from '@mxpio/bridge';
import setting from '@/settings/index';
import pages from '@/router';
import type { VAxiosOptions } from '@mxpio/types';
import type { App } from 'vue';

/**
 * 初始化项目配置项
 * 该方法内会在桥接组件实例化前执行
 */
export default async function initApplication() {
  const [{ registerSetting }, { registerRouter }] = await Promise.all([
    import('@mxpio/settings'),
    // import('@mxpio/stores'),
    import('@mxpio/router-config'),
    // import('@mxpio/common'),
  ]);

  // 初始化项目内自定义配置
  registerSetting(setting);
  // 初始化项目内自定义store
  // registerStore(storesInstance);

  // 初始化项目组件地址
  registerRouter({
    pages: pages,
  });
}

export async function setupBridge(app: App) {
  const [{ defHttp }, { router }] = await Promise.all([
    import('@mxpio/request'),
    // import('@mxpio/stores'),
    import('@mxpio/router'),
  ]);

  // 为了解耦 `packages/*` 下面各模块与stores、router、request 的依赖关系，不再相互依赖
  // 优先执行桥接模块，保证后续组件初始化正常
  // 初始化桥接模块
  return initBridge(app, {
    // stores: storesInstance,
    router: router,
    http: defHttp as unknown as VAxiosOptions,
    env: import.meta.env,
  });
}

/**
 * 初始化项目内私有化配置,例如自定义组件等
 * 该方法内会在桥接实例化后执行，执行需要依赖Commom组件的项目，避免依赖不存在报错
 */
export async function initPackages() {
  // 后续组件待桥接初始化完成再加载
  const [{ registerComponent }, { registerComponent: registerBizComponent }] = await Promise.all([
    import('@mxpio/common'),
    import('@mxpio/bizcommon'),
  ]);
  // 初始化项目内自定义组件
  registerComponent();
  // 初始化项目内自定义业务组件
  registerBizComponent();
}

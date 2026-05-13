import 'uno.css';
import '@mxpio/design';
import '@mxpio/components/src/components/VxeTable/src/css/index.scss';
import 'ant-design-vue/dist/reset.css';
// Register icon sprite
import 'virtual:svg-icons-register';
import { createApp } from 'vue';
import initApplication, { initPackages, setupBridge } from './initApplication';
import { setupI18n } from '@/locales';
import App from './App.vue';

async function bootstrap() {
  const app = createApp(App);

  // 初始化项目内自定义setting、store
  await initApplication();

  // 初始化组件通用组件
  await setupBridge(app);

  // install
  // 其他依赖项初始化操作需要放到通用之后
  const [
    { setupStore },
    { initAppConfigStore, setupErrorHandle },
    { setupGlobComp },
    { setupRouterGuard, router, setupRouter },
    { setupGlobDirectives },
  ] = await Promise.all([
    import('@mxpio/stores'),
    import('@mxpio/logics'),
    import('@mxpio/components'),
    import('@mxpio/router'),
    import('@mxpio/directives'),
  ]);

  // Configure store
  // 配置 store
  setupStore(app);

  // Initialize internal system configuration
  // 初始化内部系统配置
  // setting文件可根据实际项目需要配置
  initAppConfigStore();

  // Register global components
  // 注册全局组件
  setupGlobComp(app);

  // Multilingual configuration
  // 多语言配置
  // Asynchronous case: language files may be obtained from the server side
  // 异步案例：语言文件可能从服务器端获取
  await setupI18n(app);

  // router-guard
  // 路由守卫
  setupRouterGuard(router);

  // Register global directive
  // 注册全局指令
  setupGlobDirectives(app);

  // Configure global error handling
  // 配置全局错误处理
  setupErrorHandle(app);

  // 因部分组件内依赖国际化，需要在国际化之后执行
  await initPackages();

  // Configure routing
  // 配置路由
  setupRouter(app);

  // https://next.router.vuejs.org/api/#isready
  // await router.isReady();
  app.mount('#app');
}

bootstrap();

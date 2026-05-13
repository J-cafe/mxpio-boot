import type { RouteRecordRaw } from 'vue-router';
import type { App } from 'vue';
import { basicRoutes } from '@mxpio/router-config';
import { createRouter, createWebHashHistory } from 'vue-router';

const env = import.meta.env;

// app router
// 创建一个可以被 Vue 应用程序使用的路由实例
export const router = createRouter({
  // 创建一个 hash 历史记录。
  history: createWebHashHistory(env.VITE_PUBLIC_PATH),
  // 应该添加到路由的初始路由列表。
  routes: basicRoutes as unknown as RouteRecordRaw[],
  // 是否应该禁止尾部斜杠。默认为假
  strict: true,
  scrollBehavior: () => ({ left: 0, top: 0 }),
});

// config router
// 配置路由器
export function setupRouter(app: App<Element>) {
  app.use(router);
}

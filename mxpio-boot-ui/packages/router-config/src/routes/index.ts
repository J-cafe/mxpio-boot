import type { AppRouteRecordRaw, AppRouteModule } from '@mxpio/types';

import { PAGE_NOT_FOUND_ROUTE, REDIRECT_ROUTE } from './basic';

import { PageEnum } from '@mxpio/enums';
import { $t } from '@mxpio/locales';
import PAGES from '../component';

const routeModuleList: AppRouteModule[] = [];

const modules = import.meta.glob('./modules/**/*.ts', { eager: true });
// 加入到路由集合中
Object.keys(modules).forEach((key) => {
  const mod = (modules as Recordable)[key].default || {};
  const modList = Array.isArray(mod) ? [...mod] : [mod];
  routeModuleList.push(...modList);
});

export const asyncRoutes = [PAGE_NOT_FOUND_ROUTE, ...routeModuleList];

// 根路由
export const RootRoute: AppRouteRecordRaw = {
  path: PageEnum.ROOT,
  name: 'Root',
  redirect: PageEnum.BASE_HOME,
  meta: {
    title: 'Root',
  },
};

export const LoginRoute: AppRouteRecordRaw = {
  path: '/login',
  name: 'Login',
  // component: () => import('@/views/sys/login/Login.vue'),
  component: () => PAGES.getPage('Login'),
  // component: () =>
  //   new Promise((resolve) => {
  //     resolve(PAGES.getPage('Login')());
  //   }),
  meta: {
    title: $t('routes.basic.login'),
  },
};

export const LoginSSORoute: AppRouteRecordRaw = {
  path: '/loginSSO',
  name: 'LoginSSO',
  component: () =>
    new Promise((resolve) => {
      resolve(PAGES.getPage('LoginSSO'));
    }),
  meta: {
    title: $t('routes.basic.loginSSO'),
  },
};
export const HomePageRoute: AppRouteRecordRaw = {
  path: '/homePage',
  name: 'HomePage',
  component: () => PAGES.getPage('HomePage'),
  meta: {
    title: $t('routes.basic.homePage'),
  },
};

// Basic routing without permission
// 基本路由
export const basicRoutes = [
  LoginRoute,
  LoginSSORoute,
  HomePageRoute,
  RootRoute,
  REDIRECT_ROUTE,
  PAGE_NOT_FOUND_ROUTE,
  ...routeModuleList,
];

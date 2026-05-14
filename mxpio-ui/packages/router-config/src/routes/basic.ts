import type { AppRouteRecordRaw } from '@mxpio/types';
import PAGES from '../component';
import { RouterEnum } from '@mxpio/enums';
import { $t } from '@mxpio/locales';

// 404 on a page
export const PAGE_NOT_FOUND_ROUTE: AppRouteRecordRaw = {
  path: '/:path(.*)*',
  name: RouterEnum.PAGE_NOT_FOUND_NAME as string,
  component: () =>
    new Promise((resolve) => {
      resolve(PAGES.getPage('LAYOUT'));
    }),
  // Promise.resolve().then(() => {
  //   return PAGES.getPage('LAYOUT')();
  // }),
  meta: {
    title: 'ErrorPage',
    hideBreadcrumb: true,
    hideMenu: true,
  },
  children: [
    {
      path: '/:path(.*)*',
      name: RouterEnum.PAGE_NOT_FOUND_NAME as string,
      component: () =>
        new Promise((resolve) => {
          resolve(PAGES.getPage('EXCEPTION_COMPONENT'));
        }),
      meta: {
        title: 'ErrorPage',
        hideBreadcrumb: true,
        hideMenu: true,
      },
    },
  ],
};

export const REDIRECT_ROUTE: AppRouteRecordRaw = {
  path: '/redirect',
  component: () =>
    new Promise((resolve) => {
      resolve(PAGES.getPage('LAYOUT'));
    }),
  name: 'RedirectTo',
  meta: {
    title: RouterEnum.REDIRECT_NAME as string,
    hideBreadcrumb: true,
    hideMenu: true,
  },
  children: [
    {
      path: '/redirect/:path(.*)/:_redirect_type(.*)/:_origin_params(.*)?',
      name: RouterEnum.REDIRECT_NAME as string,
      component: () =>
        new Promise((resolve) => {
          resolve(PAGES.getPage('REDIRECT'));
        }),
      meta: {
        title: RouterEnum.REDIRECT_NAME as string,
        hideBreadcrumb: true,
      },
    },
  ],
};

export const ERROR_LOG_ROUTE: AppRouteRecordRaw = {
  path: '/error-log',
  name: 'ErrorLog',
  component: () =>
    new Promise((resolve) => {
      resolve(PAGES.getPage('LAYOUT'));
    }),
  redirect: '/error-log/list',
  meta: {
    title: 'ErrorLog',
    hideBreadcrumb: true,
    hideChildrenInMenu: true,
  },
  children: [
    {
      path: 'list',
      name: 'ErrorLogList',
      component: () =>
        new Promise((resolve) => {
          resolve(PAGES.getPage('ERROR_LOG'));
        }),
      meta: {
        title: $t('routes.basic.errorLogList'),
        hideBreadcrumb: true,
        currentActiveMenu: '/error-log',
      },
    },
  ],
};

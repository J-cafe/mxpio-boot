import type { AppRouteModule } from '@mxpio/types';

import PAGES from '../../component';

const dashboard: AppRouteModule = {
  path: '/dashboard',
  name: 'Dashboard',
  component: () =>
    Promise.resolve().then(() => {
      return PAGES.getPage('LAYOUT');
    }),
  redirect: '/dashboard/analysis',
  meta: {
    orderNo: 10,
    icon: 'ion:grid-outline',
    title: '首页',
  },
  children: [
    {
      path: 'analysis',
      name: 'Analysis',
      component: () =>
        Promise.resolve().then(() => {
          return PAGES.getPage('analysis');
        }),
      meta: {
        title: '首页',
      },
    },
  ],
};

export default dashboard;

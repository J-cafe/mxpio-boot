import type { AppRouteRecordRaw } from '@mxpio/types';
import { $t } from '@mxpio/locales';
import PAGES from '../../component';

const account: AppRouteRecordRaw = {
  path: '/account',
  name: 'Account',
  component: () =>
    Promise.resolve().then(() => {
      return PAGES.getPage('LAYOUT');
    }),
  meta: {
    title: $t('routes.account.account'),
    hideMenu: true,
  },
  children: [
    {
      path: 'center',
      name: 'AccountCenter',
      component: () =>
        Promise.resolve().then(() => {
          return PAGES.getPage('AccountCenter');
        }),
      meta: {
        title: '个人中心',
        hideBreadcrumb: true,
        hideChildrenInMenu: true,
        hideMenu: true,
      },
    },
  ],
};
export default account;

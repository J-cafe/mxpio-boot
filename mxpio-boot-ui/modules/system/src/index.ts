import system from './views/system';
import sys from './views/sys';
import { setupI18n } from './locales';

const account = {
  AccountCenter: () => import('./views/account/setting/index.vue'),
};

const dashboard = {
  analysis: () => import('./views/dashboard/analysis/index.vue'),
};

const monitor = {
  monitor: () => import('./views/monitor/server/index.vue'),
};

const message = {
  message: () => import('./views/message/index.vue'),
};

// 注册i18n
setupI18n();

export default {
  ...account,
  ...dashboard,
  ...system,
  ...sys,
  ...monitor,
  ...message,
};

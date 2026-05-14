import type { Router } from 'vue-router';
import { useMultipleTabStore, useUserStore, useAppStore, usePermissionStore } from '@mxpio/stores';

import { PageEnum } from '@mxpio/enums';
import { removeTabChangeListener } from '@mxpio/logics/src/mitt/routeChange';

export function createStateGuard(router: Router) {
  router.afterEach((to) => {
    // Just enter the login page and clear the authentication information
    if (to.path === PageEnum.BASE_LOGIN) {
      const tabStore = useMultipleTabStore();
      const userStore = useUserStore();
      const appStore = useAppStore();
      const permissionStore = usePermissionStore();
      appStore.resetAllState();
      permissionStore.resetState();
      tabStore.resetState();
      userStore.resetState();
      removeTabChangeListener();
    }
  });
}

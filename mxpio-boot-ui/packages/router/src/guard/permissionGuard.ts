import type { Router } from 'vue-router';
import {
  useUserStoreWithOut,
  useSystemStoreWithOut,
  usePermissionStoreWithOut,
} from '@mxpio/stores';

import { PageEnum, RouterEnum } from '@mxpio/enums';
// import { PAGE_NOT_FOUND_ROUTE } from '../routes/basic';

// import { RootRoute } from '../routes/index';

const env = import.meta.env;
const { VITE_GLOB_APP_SSO: enableSSO } = env;

const LOGIN_PATH = !enableSSO ? PageEnum.BASE_LOGIN : PageEnum.BASE_LOGIN_SSO;

const ROOT_PATH = PageEnum.ROOT;

const whitePathList: PageEnum[] = [LOGIN_PATH];
export function createPermissionGuard(router: Router) {
  const userStore = useUserStoreWithOut();
  const systemStore = useSystemStoreWithOut();
  const permissionStore = usePermissionStoreWithOut();
  router.beforeEach(async (to, from, next) => {
    if (
      from.path === ROOT_PATH &&
      to.path === PageEnum.BASE_HOME &&
      userStore.getUserInfo.homePath &&
      userStore.getUserInfo.homePath !== PageEnum.BASE_HOME
    ) {
      next(userStore.getUserInfo.homePath);
      return;
    }

    const token = userStore.getToken;

    // Whitelist can be directly entered
    if (whitePathList.includes(to.path as PageEnum)) {
      if (to.path === LOGIN_PATH && token) {
        const isSessionTimeout = userStore.getSessionTimeout;
        try {
          await userStore.afterLoginAction();
          if (!isSessionTimeout) {
            next((to.query?.redirect as string) || '/');
            return;
          }
        } catch {
          //
        }
      }

      next();
      return;
    }
    // token or user does not exist
    if (!token) {
      // You can access without permission. You need to set the routing meta.ignoreAuth to true
      if (to.meta.ignoreAuth) {
        next();
        return;
      }

      // redirect login page
      const redirectData: { path: string; replace: boolean; query?: Recordable<string> } = {
        path: LOGIN_PATH,
        replace: true,
      };
      if (to.path) {
        redirectData.query = {
          ...redirectData.query,
          redirect: to.path,
        };
      }
      next(redirectData);
      return;
    }
    // Jump to the 404 page after processing the login
    if (
      from.path === LOGIN_PATH &&
      to.name === RouterEnum.PAGE_NOT_FOUND_NAME &&
      to.fullPath !== (userStore.getUserInfo.homePath || PageEnum.BASE_HOME)
    ) {
      next(userStore.getUserInfo.homePath || PageEnum.BASE_HOME);
      return;
    }
    // get userinfo while last fetch time is empty
    if (userStore.getLastUpdateTime === 0) {
      try {
        await userStore.getUserInfoAction();
        await systemStore.getSystemAction();
      } catch (err) {
        next();
        return;
      }
    }

    if (permissionStore.getIsDynamicAddedRoute) {
      // redirect to the saved redirect path xys 20260313
      const redirectPath = from.query.redirect as string;
      if (redirectPath && redirectPath !== to.path) {
        const redirect = decodeURIComponent(redirectPath);
        const nextData = { path: redirect };
        next(nextData);
        return;
      }

      next();
      return;
    }

    // const routes = await permissionStore.buildRoutesAction();
    // routes.forEach((route: RouteRecordRaw) => {
    //   router.addRoute(route as unknown as RouteRecordRaw);
    // });

    await permissionStore.addRoutesAction();
    // router.addRoute(PAGE_NOT_FOUND_ROUTE as unknown as RouteRecordRaw);
    permissionStore.setDynamicAddedRoute(true);
    if (to.name === RouterEnum.PAGE_NOT_FOUND_NAME) {
      // 动态添加路由后，此处应当重定向到fullPath，否则会加载404页面内容
      next({ path: to.fullPath, replace: true, query: to.query });
    } else {
      const redirectPath = (from.query.redirect || to.path) as string;
      const redirect = decodeURIComponent(redirectPath);
      const nextData = to.path === redirect ? { ...to, replace: true } : { path: redirect };
      next(nextData);
    }
  });
}

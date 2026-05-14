import type { AppRouteModule, AppRouteRecordRaw } from '@mxpio/types';
import type { Router, RouteRecordNormalized } from 'vue-router';
import { cloneDeep, omit } from 'lodash-es';
import { createRouter, createWebHashHistory } from 'vue-router';
import { warn, isHttpUrl } from '@mxpio/utils';
import { RouterEnum } from '@mxpio/enums';
import PAGES from '../component';

// export type LayoutMapKey = 'LAYOUT';

/**
 * @description: parent-layout
 */
export const getParentLayout = (_name?: string) => {
  return () =>
    new Promise((resolve) => {
      resolve({
        name: _name || RouterEnum.PARENT_LAYOUT_NAME,
      });
    });
};

// let dynamicViewsModules: Record<string, () => Promise<Recordable>>;

// Dynamic introduction
function asyncImportRoute(routes: AppRouteRecordRaw[] | undefined) {
  // dynamicViewsModules = dynamicViewsModules || import.meta.glob('../../views/**/*.{vue,tsx}');
  if (!routes) return;

  routes.forEach((item) => {
    if (item.meta?.frameSrc && isHttpUrl(item.meta?.frameSrc)) {
      if (!item.meta?.outside) {
        item.component = 'IFRAME';
      } else {
        item.component = 'OUTSIDE';
      }
    }
    const { component, name } = item;
    const { children } = item;
    if (component) {
      if (PAGES.hasPage(component)) {
        item.component = () => PAGES.getPage(component);
      } else if (component.toUpperCase() === 'BLANK') {
        item.component = () => PAGES.getPage('LAYOUT');
      } else {
        item.component = '';
        // item.component = dynamicImport(dynamicViewsModules, component as string);
      }
    } else if (name) {
      item.component = getParentLayout();
    }
    children && asyncImportRoute(children);
  });
}

// function dynamicImport(
//   dynamicViewsModules: Record<string, () => Promise<Recordable>>,
//   component: string,
// ) {
//   const keys = Object.keys(dynamicViewsModules);
//   const matchKeys = keys.filter((key) => {
//     const k = key.replace('../../views', '');
//     const startFlag = component.startsWith('/');
//     const endFlag = component.endsWith('.vue') || component.endsWith('.tsx');
//     const startIndex = startFlag ? 0 : 1;
//     const lastIndex = endFlag ? k.length : k.lastIndexOf('.');
//     return k.substring(startIndex, lastIndex) === component;
//   });
//   if (matchKeys?.length === 1) {
//     const matchKey = matchKeys[0];
//     return dynamicViewsModules[matchKey];
//   } else if (matchKeys?.length > 1) {
//     warn(
//       'Please do not create `.vue` and `.TSX` files with the same file name in the same hierarchical directory under the views folder. This will cause dynamic introduction failure',
//     );
//     return;
//   } else {
//     warn('在src/views/下找不到`' + component + '.vue` 或 `' + component + '.tsx`, 请自行创建!');
//     return PAGES.getPage('EXCEPTION_COMPONENT');
//     // return EXCEPTION_COMPONENT;
//   }
// }

// Turn background objects into routing objects
// 将对象变成路由对象
export function transformObjToRoute<T = AppRouteModule>(routeList: AppRouteModule[]): T[] {
  const LAYOUT = () => PAGES.getPage('LAYOUT');
  routeList.forEach((route) => {
    const component = route.component as string;
    if (component) {
      if (component.toUpperCase() === 'BLANK') {
        // route.component = LayoutMap.get(component.toUpperCase());
        route.component = LAYOUT;
      } else {
        route.children = [cloneDeep(route)];
        route.component = LAYOUT;
        //某些情况下如果name如果没有值， 多个一级路由菜单会导致页面404
        if (!route.name) {
          warn('找不到菜单对应的name, 请检查数据!' + JSON.stringify(route));
        }
        route.name = `${route.name}Parent`;
        route.path = '';
        const meta = route.meta || {};
        meta.single = true;
        meta.affix = false;
        route.meta = meta;
      }
    } else {
      warn('请正确配置路由：' + route?.name + '的component属性');
    }
    route.children && asyncImportRoute(route.children);
  });
  return routeList as unknown as T[];
}

/**
 * Convert multi-level routing to level 2 routing
 * 将多级路由转换为 2 级路由
 */
export function flatMultiLevelRoutes(routeModules: AppRouteModule[]) {
  const modules: AppRouteModule[] = cloneDeep(routeModules);

  for (let index = 0; index < modules.length; index++) {
    const routeModule = modules[index];
    // 判断级别是否 多级 路由
    if (!isMultipleRoute(routeModule)) {
      // 声明终止当前循环， 即跳过此次循环，进行下一轮
      continue;
    }
    // 路由等级提升
    promoteRouteLevel(routeModule);
  }
  return modules;
}

// Routing level upgrade
// 路由等级提升
function promoteRouteLevel(routeModule: AppRouteModule) {
  // Use vue-router to splice menus
  // 使用vue-router拼接菜单
  // createRouter 创建一个可以被 Vue 应用程序使用的路由实例
  let router: Router | null = createRouter({
    routes: [routeModule as unknown as RouteRecordNormalized],
    history: createWebHashHistory(),
  });
  // getRoutes： 获取所有 路由记录的完整列表。
  const routes = router.getRoutes();
  // 将所有子路由添加到二级路由
  addToChildren(routes, routeModule.children || [], routeModule);
  router = null;

  // omit lodash的函数 对传入的item对象的children进行删除
  routeModule.children = routeModule.children?.map((item) => omit(item, 'children'));
}

// Add all sub-routes to the secondary route
// 将所有子路由添加到二级路由
function addToChildren(
  routes: RouteRecordNormalized[],
  children: AppRouteRecordRaw[],
  routeModule: AppRouteModule,
) {
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    const route = routes.find((item) => item.name === child.name);
    if (!route) {
      continue;
    }
    routeModule.children = routeModule.children || [];
    if (!routeModule.children.find((item) => item.name === route.name)) {
      routeModule.children?.push(route as unknown as AppRouteModule);
    }
    if (child.children?.length) {
      addToChildren(routes, child.children, routeModule);
    }
  }
}

// Determine whether the level exceeds 2 levels
// 判断级别是否超过2级
function isMultipleRoute(routeModule: AppRouteModule) {
  // Reflect.has 与 in 操作符 相同, 用于检查一个对象(包括它原型链上)是否拥有某个属性
  if (!routeModule || !Reflect.has(routeModule, 'children') || !routeModule.children?.length) {
    return false;
  }

  const children = routeModule.children;

  let flag = false;
  for (let index = 0; index < children.length; index++) {
    const child = children[index];
    if (child.children?.length) {
      flag = true;
      break;
    }
  }
  return flag;
}

/**
 * mxpio-boot
 * @description 一级菜单拼接/，防止报错
 * */
export function joinNodePath(menus: any[]): any[] {
  menus.forEach((menu) => {
    if (!(menu.path.startsWith('/') || isHttpUrl(menu.path))) {
      menu.path = '/' + menu.path;
    }
  });
  return menus;
}

/**
 * mxpio-boot
 * @description 转换请求路由菜单数据格式为满足vben格式
 * */
export function convertRoutes(menus: any[]): AppRouteRecordRaw[] {
  const routes: AppRouteRecordRaw[] = [];
  menus.forEach((menu) => {
    const route: AppRouteRecordRaw = {
      // name: menu.key,
      name: menu.name ? menu.name : menu.path ? menu.path.split('/').join('-') : menu.key, //name必须与组件defineOptions定义的一致
      // path: !isHttpUrl(menu.component) || menu.meta.outside ? menu.path : menu.key,
      path: isHttpUrl(menu.path) ? menu.key : menu.path,
      key: menu.key,
      component: menu.component,
      meta: {
        title: menu.meta.title,
        icon: menu.meta.icon,
        orderNo: menu.meta.order,
        frameSrc: isHttpUrl(menu.path) && menu.path,
        hideMenu: menu.meta.hidden,
        key: menu.key,
        ignoreKeepAlive: !menu.meta.keepAlive,
        urlScope: menu.meta.urlScope,
        outside: menu.meta.outside,
      },
    };
    if (menu?.children?.length) {
      route.children = convertRoutes(menu?.children);
    }
    // if (menu.meta.urlType !== 'C' && route.children?.length) {
    //   const firstChildPath = getFirstChildRoutePath(route.children, route.path);
    //   if (firstChildPath) {
    //     route.redirect = firstChildPath;
    //   }
    // }
    routes.push(route);
  });
  return routes;
}

// 路径处理
export function joinParentPath(routes: AppRouteRecordRaw[], parentPath = '') {
  for (let index = 0; index < routes.length; index++) {
    const route = routes[index];
    // https://next.router.vuejs.org/guide/essentials/nested-routes.html
    // Note that nested paths that start with / will be treated as a root path.
    // 请注意，以 / 开头的嵌套路径将被视为根路径。
    // This allows you to leverage the component nesting without having to use a nested URL.
    // 这允许你利用组件嵌套，而无需使用嵌套 URL。
    if (!(route.path.startsWith('/') || isHttpUrl(route.path))) {
      // path doesn't start with /, nor is it a url, join parent path
      // 路径不以 / 开头，也不是 url，加入父路径
      route.path = `${parentPath}/${route.path}`;
    }
    if (route.meta?.urlType !== 'C' && route.children?.length) {
      const firstChildPath = getFirstChildRoutePath(route.children, route.path);
      if (firstChildPath) {
        route.redirect = firstChildPath;
      }
    }
    if (route?.children?.length) {
      joinParentPath(route.children, route.meta?.hidePathForChildren ? parentPath : route.path);
    }
  }
}

// 辅助函数：递归获取完整的子路由路径
function getFullRoutePath(route: AppRouteRecordRaw, parentPath: string = ''): string {
  let fullPath = route.path;
  // 如果路径不是以/开头，需要拼接父级路径
  if (fullPath && !fullPath.startsWith('/')) {
    // 确保父级路径以/结尾
    const parentPathWithSlash = parentPath.endsWith('/') ? parentPath : `${parentPath}/`;
    fullPath = `${parentPathWithSlash}${fullPath}`;
  }
  return fullPath;
}

//
export const getFirstChildRoutePath = (
  routes: AppRouteRecordRaw[],
  parentPath: string = '',
): string | null => {
  // 递归获取第一个子路由的路径, 排除掉隐藏的路由, 直到找到第一个非隐藏的路由, 返回其真实路径，需要拼接其父级路由的路径
  if (!routes || routes.length === 0) {
    return null;
  }

  for (const route of routes) {
    // 需要排除掉隐藏的路由
    if (route.meta?.hideMenu) {
      continue;
    }

    // 获取当前路由的完整路径
    const currentPath = getFullRoutePath(route, parentPath);
    if (!route.children || route.children.length === 0) {
      // 如果没有子路由，返回当前路由的完整路径
      return currentPath;
    } else {
      // 如果有子路由，递归查找子路由的第一个路径
      const childPath = getFirstChildRoutePath(route.children, currentPath);
      if (childPath) {
        return childPath;
      }
    }
  }

  // 如果所有路由都隐藏或没有可用路径，返回null
  return null;
};

/**
 * mxpio-boot
 * @description 后台返回的路由菜单默认插入首页
 * */
// export function addHomeRoutes(routes: AppRouteRecordRaw[]): AppRouteRecordRaw[] {
//   routes.unshift(dashboard);
//   return routes;
// }

// /**
//  * mxpio-boot
//  * @description 后台返回的路由菜单默认插入个人页
//  * */
// export function addAccountRoutes(routes: AppRouteRecordRaw[]): AppRouteRecordRaw[] {
//   routes.unshift(account);
//   return routes;
// }

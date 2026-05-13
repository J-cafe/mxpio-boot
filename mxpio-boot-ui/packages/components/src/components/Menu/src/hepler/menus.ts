import type { Menu, MenuModule } from '@mxpio/types';
import type { RouteRecordNormalized } from 'vue-router';
import { useBridge } from '@mxpio/bridge';
import { useAppStoreWithOut, usePermissionStore } from '@mxpio/stores';
import { transformMenuModule, getAllParentPath } from '@mxpio/router-config';
import { filter, isHttpUrl } from '@mxpio/utils';
import { PermissionModeEnum, UrlScopeEnum } from '@mxpio/enums';
import { pathToRegexp } from 'path-to-regexp';
import { cloneDeep } from 'lodash-es';

const menuModules: MenuModule[] = [];

// xys 20250919移除
// const modules = import.meta.glob('./modules/**/*.ts', { eager: true });
// Object.keys(modules).forEach((key) => {
//   const mod = (modules as Recordable)[key].default || {};
//   const modList = Array.isArray(mod) ? [...mod] : [mod];
//   menuModules.push(...modList);
// });

// ===========================
// ==========Helper===========
// ===========================

const getPermissionMode = () => {
  const appStore = useAppStoreWithOut();
  return appStore.getProjectConfig.permissionMode;
};

const isBackMode = () => {
  return getPermissionMode() === PermissionModeEnum.BACK;
};

const isRouteMappingMode = () => {
  return getPermissionMode() === PermissionModeEnum.ROUTE_MAPPING;
};

const isRoleMode = () => {
  return getPermissionMode() === PermissionModeEnum.ROLE;
};

const staticMenus: Menu[] = [];
(() => {
  menuModules.sort((a, b) => {
    return (a.orderNo || 0) - (b.orderNo || 0);
  });

  for (const menu of menuModules) {
    staticMenus.push(transformMenuModule(menu));
  }
})();

async function getAsyncMenus() {
  const permissionStore = usePermissionStore();
  //递归过滤所有隐藏的菜单
  const menuFilter = (items) => {
    return items.filter((item) => {
      const show = !item.meta?.hideMenu && !item.hideMenu;
      if (show && item.children) {
        item.children = menuFilter(item.children);
      }
      return show;
    });
  };
  if (isBackMode()) {
    return menuFilter(permissionStore.getBackMenuList);
  }
  if (isRouteMappingMode()) {
    return menuFilter(permissionStore.getFrontMenuList);
  }

  return staticMenus;
}

export const getMenus = async (): Promise<Menu[]> => {
  const menus = await getAsyncMenus();
  if (isRoleMode()) {
    const { router } = useBridge();
    const routes = router.getRoutes();
    return filter(menus, basicFilter(routes));
  }
  return menus;
};

export async function getCurrentParentPath(currentPath: string) {
  const menus = await getAsyncMenus();
  const allParentPath = await getAllParentPath(menus, currentPath);
  return allParentPath?.[0];
}

export async function getCurrentAllParentPath(currentPath: string) {
  const menus = await getAsyncMenus();
  const allParentPath = await getAllParentPath(menus, currentPath);
  return allParentPath;
}

// Get the level 1 menu, delete children
export async function getShallowMenus(): Promise<Menu[]> {
  const menus = await getAsyncMenus();
  const shallowMenuList = menus.map((item) => ({ ...item, children: undefined }));
  if (isRoleMode()) {
    const { router } = useBridge();
    const routes = router.getRoutes();
    return shallowMenuList.filter(basicFilter(routes));
  }
  return shallowMenuList;
}

// Get the modules menu of the platform, delete children
export async function getModulesMenus(): Promise<Menu[]> {
  const { router } = useBridge();
  const { currentRoute } = router;
  const currentPath = currentRoute.value.path;
  let platformMenu = await getParentMenusByPath(currentPath);
  if (!platformMenu) {
    return [];
  }
  platformMenu = cloneDeep(platformMenu);
  // 根据当前路由对应平台下模块列表
  platformMenu.children = platformMenu?.children
    ?.filter(
      (child) => child.meta?.urlScope === UrlScopeEnum.MODULES && child.meta?.hideMenu !== true,
    )
    .map((item) => ({ ...item, children: undefined }));
  return platformMenu.children || [];
}

// Get the children of the Modules menu of the platform
export async function getModulesChildrenMenus(): Promise<Menu[]> {
  const { router } = useBridge();
  const { currentRoute } = router;
  const currentPath = currentRoute.value.path;
  let platformMenu = await getParentMenusByPath(currentPath);
  if (!platformMenu) {
    return [];
  }
  platformMenu = cloneDeep(platformMenu);
  // 根据当前路由对应平台下模块列表
  platformMenu.children = platformMenu.children
    ?.filter(
      (child) => child.meta?.urlScope === UrlScopeEnum.MODULES && child.meta?.hideMenu !== true,
    )
    .map((item) => ({ ...item }));
  const modulesMenus = platformMenu.children || [];
  // 遍历当前模块及子模块，查询到当前路由对应的Modules模块，返回对应Modules模块的子菜单

  // 获取所有父级路径
  const allParentPaths = await getCurrentAllParentPath(currentPath);
  // 如果没有父级路径，返回空数组
  if (!allParentPaths || allParentPaths.length === 0) {
    return [];
  }

  // 查找当前路径对应的 Modules 菜单项
  let targetModule: Menu | undefined;

  // 遍历所有 Modules 菜单，找到包含当前路径的模块
  for (const module of modulesMenus) {
    // 获取模块下的所有菜单（包括嵌套的子菜单）
    const moduleChildrenMenus = flattenMenuChildren(module?.children || []);
    // 检查当前路径是否在该模块的子菜单中
    const found = moduleChildrenMenus.some((child) => {
      // 检查当前路径是否匹配
      if (child.path === currentPath) {
        return true;
      }
      // 检查当前路径是否是该菜单的父路径（即该菜单的子路径）
      if (currentPath.startsWith(child.path + '/')) {
        return true;
      }
      return false;
    });
    if (found) {
      targetModule = module;
      break;
    }
  }
  // 如果没有找到对应的模块，返回空数组
  if (!targetModule) {
    return [];
  }

  // 过滤掉隐藏的菜单
  return targetModule.children?.filter((item) => !item.meta?.hideMenu && !item.hideMenu) || [];
}

// 根据当前路由、子级path获取模块菜单,返回顶层菜单
async function getParentMenusByPath(path: string): Promise<Menu> {
  const menus = await getAsyncMenus();
  const parentPath = await getCurrentParentPath(path);
  const parentMenu = menus.find((item) => item.path === parentPath);

  // if (!parentMenu) return {};
  return parentMenu;
}

// Get the children of the menu
export async function getChildrenMenus(parentPath: string, recursive = false): Promise<Menu[]> {
  const menus = await getMenus();
  const parent = menus.find((item) => item.path === parentPath);
  if (!parent || !parent.children || !!parent?.meta?.hideChildrenInMenu) {
    return [] as Menu[];
  }

  let children = parent.children;

  // 如果需要递归获取所有子菜单
  if (recursive) {
    children = flattenMenuChildren(parent.children);
  }

  if (isRoleMode()) {
    const { router } = useBridge();
    const routes = router.getRoutes();
    return filter(children, basicFilter(routes));
  }
  return children;
}

// 扁平化菜单的所有子孙节点
function flattenMenuChildren(menus: Menu[]): Menu[] {
  const result: Menu[] = [];

  function traverse(items: Menu[]) {
    for (const item of items) {
      // 添加当前菜单
      result.push(item);

      // 如果有子菜单，递归遍历
      if (item.children && item.children.length > 0) {
        traverse(item.children);
      }
    }
  }

  traverse(menus);
  return result;
}

function basicFilter(routes: RouteRecordNormalized[]) {
  return (menu: Menu) => {
    const matchRoute = routes.find((route) => {
      if (isHttpUrl(menu.path)) return true;

      if (route.meta?.carryParam) {
        return pathToRegexp(route.path).test(menu.path);
      }
      const isSame = route.path === menu.path;
      if (!isSame) return false;

      if (route.meta?.ignoreAuth) return true;

      return isSame || pathToRegexp(route.path).test(menu.path);
    });

    if (!matchRoute) return false;
    menu.icon = (menu.icon || matchRoute.meta.icon) as string;
    menu.meta = matchRoute.meta;
    return true;
  };
}

// xys 20240618
import { useBridge } from '@mxpio/bridge';
import { PermissionModeEnum } from '@mxpio/enums/src/appEnum';
import { RoleEnum } from '@mxpio/enums/src/roleEnum';
import { projectSetting } from '@mxpio/settings/src/projectSetting';
import { useAppStore, useMultipleTabStore, usePermissionStore, useUserStore } from '@mxpio/stores';
import { isArray } from '@mxpio/utils/src/is';
import { intersection } from 'lodash-es';

// import type { RouteRecordRaw } from 'vue-router';
import { useTabs } from './useTabs';

// User permissions related operations
export function usePermission() {
  const { router } = useBridge();
  const userStore = useUserStore();
  const appStore = useAppStore();
  const permissionStore = usePermissionStore();
  const { closeAll } = useTabs(router);

  /**
   * Change permission mode
   */
  async function togglePermissionMode() {
    appStore.setProjectConfig({
      permissionMode:
        appStore.projectConfig?.permissionMode === PermissionModeEnum.BACK
          ? PermissionModeEnum.ROUTE_MAPPING
          : PermissionModeEnum.BACK,
    });
    location.reload();
  }

  /**
   * Reset and regain authority resource information
   * 重置和重新获得权限资源信息
   * @param id
   */
  async function resume() {
    const tabStore = useMultipleTabStore();
    tabStore.clearCacheTabs();
    // resetRouter();
    // const routes = await permissionStore.buildRoutesAction();
    // routes.forEach((route) => {
    //   router.addRoute(route as unknown as RouteRecordRaw);
    // });
    await permissionStore.addRoutesAction();
    permissionStore.setLastBuildMenuTime();
    closeAll();
  }

  /**
   * Determine whether there is permission
   */
  function hasPermission(value?: RoleEnum | RoleEnum[] | string | string[], def = true): boolean {
    // Visible by default
    if (!value) {
      return def;
    }

    const permMode = appStore.getProjectConfig.permissionMode;

    if ([PermissionModeEnum.ROUTE_MAPPING, PermissionModeEnum.ROLE].includes(permMode)) {
      if (!isArray(value)) {
        return userStore.getRoleList?.includes(value as RoleEnum);
      }
      return (intersection(value, userStore.getRoleList) as RoleEnum[]).length > 0;
    }

    if (PermissionModeEnum.BACK === permMode) {
      // 拥有的权限列表
      const allCodeList = permissionStore.getPermCodeList as string[];
      // 全部权限列表
      const allPermCodeList = permissionStore.allPermCodeList as string[];
      if (!isArray(value)) {
        const splits = ['||', '&&'];
        const splitName = splits.find((item) => value.includes(item));
        if (splitName) {
          const splitCodes = value.split(splitName);
          return splitName === splits[0]
            ? intersection(splitCodes, allCodeList).length > 0
            : intersection(splitCodes, allCodeList).length === splitCodes.length;
        }
        // 全部权限里未找到该标识。默认为拥有权限
        if (!allPermCodeList.includes(value)) {
          return true;
        }
        return allCodeList.includes(value);
      }
      return (intersection(value, allCodeList) as string[]).length > 0;
    }
    return true;
  }

  /**
   * Change roles
   * @param roles
   */
  async function changeRole(roles: RoleEnum | RoleEnum[]): Promise<void> {
    if (projectSetting.permissionMode !== PermissionModeEnum.ROUTE_MAPPING) {
      throw new Error(
        'Please switch PermissionModeEnum to ROUTE_MAPPING mode in the configuration to operate!',
      );
    }

    if (!isArray(roles)) {
      roles = [roles];
    }
    userStore.setRoleList(roles);
    await resume();
  }

  /**
   * refresh menu data
   */
  async function refreshMenu() {
    resume();
  }

  return { changeRole, hasPermission, togglePermissionMode, refreshMenu };
}

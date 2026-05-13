import type { Menu } from '@mxpio/types';
import type { Ref } from 'vue';
import { watch, unref, ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { MenuSplitTyeEnum } from '@mxpio/enums/src/menuEnum';
import { useThrottleFn } from '@vueuse/core';
import { useMenuSetting } from '@mxpio/hooks/src/setting/useMenuSetting';
import {
  // getChildrenMenus,
  // getCurrentParentPath,
  getMenus,
  // getShallowMenus,
  getModulesMenus,
  getModulesChildrenMenus,
} from '@mxpio/components';
// import { useStore } from '@mxpio/bridge';
import { usePermissionStore } from '@mxpio/stores';
import { useAppInject } from '@mxpio/hooks/src/web/useAppInject';

export function useSplitMenu(splitType: Ref<MenuSplitTyeEnum>) {
  // Menu array
  const menusRef = ref<Menu[]>([]);
  const { currentRoute } = useRouter();
  const { getIsMobile } = useAppInject();

  // const usePermissionStore = useStore('usePermissionStore');
  const permissionStore = usePermissionStore();
  const { setMenuSetting, getIsHorizontal, getSplit } = useMenuSetting();

  const throttleHandleSplitLeftMenu = useThrottleFn(handleSplitLeftMenu, 50);

  const throttleHandleSplitTopMenu = useThrottleFn(genMenus, 50);

  const splitNotLeft = computed(
    () => unref(splitType) !== MenuSplitTyeEnum.LEFT && !unref(getIsHorizontal),
  );

  const getSplitLeft = computed(
    () => !unref(getSplit) || unref(splitType) !== MenuSplitTyeEnum.LEFT,
  );

  const getSpiltTop = computed(() => unref(splitType) === MenuSplitTyeEnum.TOP);

  const normalType = computed(() => {
    return unref(splitType) === MenuSplitTyeEnum.NONE || !unref(getSplit);
  });

  watch(
    [() => unref(currentRoute).path, () => unref(splitType)],
    async () => {
      if (unref(splitNotLeft) || unref(getIsMobile)) return;
      throttleHandleSplitLeftMenu();
    },
    {
      immediate: true,
    },
  );

  // watch(
  //   [() => unref(currentRoute).path, () => unref(splitType)],
  //   async ([path]: [string, MenuSplitTyeEnum]) => {
  //     const { meta } = unref(currentRoute);
  //     const currentActiveMenu = meta.currentActiveMenu as string;
  //     let parentPath = await getCurrentParentPath(path);
  //     if (!parentPath) {
  //       parentPath = await getCurrentParentPath(currentActiveMenu);
  //     }
  //     parentPath && throttleHandleSplitLeftMenu();
  //   },
  //   {
  //     immediate: true,
  //   },
  // );

  watch(
    [
      () => permissionStore.getLastBuildMenuTime,
      () => permissionStore.getBackMenuList,
      () => unref(currentRoute).path,
    ],
    () => {
      throttleHandleSplitTopMenu();
    },
    {
      immediate: true,
    },
  );

  // split Menu changes
  watch(
    () => getSplit.value,
    () => {
      if (unref(splitNotLeft)) return;
      throttleHandleSplitTopMenu();
    },
  );

  // Handle left menu split
  async function handleSplitLeftMenu() {
    if (unref(getSplitLeft) || unref(getIsMobile)) return;
    // spilt mode left
    // const children = await getChildrenMenus(parentPath);
    const children = await getModulesChildrenMenus();

    if (!children || !children.length) {
      setMenuSetting({ hidden: true });
      menusRef.value = [];
      return;
    }

    setMenuSetting({ hidden: false });
    menusRef.value = children;
  }

  // get menus
  async function genMenus() {
    // normal mode
    if (unref(normalType) || unref(getIsMobile)) {
      menusRef.value = await getMenus();
      return;
    }
    // split-top
    if (unref(getSpiltTop)) {
      // 获取顶部模块菜单
      const shallowMenus = await getModulesMenus();
      // const shallowMenus = await getShallowMenus();
      menusRef.value = shallowMenus;
      return;
    }
  }

  return { menusRef };
}

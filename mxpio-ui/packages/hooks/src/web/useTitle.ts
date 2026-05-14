// xys 20240614
import { RouterEnum } from '@mxpio/enums';
import { $t } from '@mxpio/locales';
import { useLocaleStore, useSystemStore } from '@mxpio/stores';
import { useTitle as usePageTitle } from '@vueuse/core';
import { unref, watch } from 'vue';
import { useRouter } from 'vue-router';

/**
 * Listening to page changes and dynamically changing site titles
 */
export function useTitle() {
  const { currentRoute } = useRouter();
  const localeStore = useLocaleStore();
  const systemStore = useSystemStore();
  const pageTitle = usePageTitle();
  watch(
    [() => currentRoute.value.path, () => localeStore.getLocale],
    () => {
      const route = unref(currentRoute);

      if (route.name === RouterEnum.REDIRECT_NAME) {
        return;
      }
      // xys 20250924 暂时移除菜单国际化
      // const tTitle = route?.meta?.title ? $t(route?.meta?.title as string) : '';
      const tTitle = route?.meta?.title
        ? route?.meta?.title.indexOf('.') > 0
          ? $t(route?.meta?.title as string)
          : route?.meta?.title
        : '';
      pageTitle.value = tTitle
        ? ` ${tTitle} - ${systemStore.appSystemDesc} `
        : `${systemStore.appSystemDesc}`;
    },
    { immediate: true },
  );
}

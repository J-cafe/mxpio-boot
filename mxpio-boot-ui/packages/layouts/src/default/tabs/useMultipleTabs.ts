import { toRaw, ref, nextTick } from 'vue';
import type { RouteLocationNormalized } from 'vue-router';
import { useDesign } from '@mxpio/hooks/src/web/useDesign';
import { useSortable } from '@mxpio/hooks/src/web/useSortable';
// import { useStore } from '@mxpio/bridge';
import { useMultipleTabStore } from '@mxpio/stores';
import { isNil } from '@mxpio/utils/src/is';
import { projectSetting } from '@mxpio/settings/src/projectSetting';
import { useRouter } from 'vue-router';
import { $t } from '@mxpio/locales';

export function initAffixTabs(): string[] {
  const affixList = ref<RouteLocationNormalized[]>([]);
  // const useMultipleTabStore = useStore('useMultipleTabStore');
  const tabStore = useMultipleTabStore();
  const router = useRouter();
  /**
   * @description: Filter all fixed routes
   */
  function filterAffixTabs(routes: RouteLocationNormalized[]) {
    const tabs: RouteLocationNormalized[] = [];
    routes &&
      routes.forEach((route) => {
        if (route.meta && route.meta.affix) {
          tabs.push(toRaw(route));
        }
      });
    return tabs;
  }

  // 20260203 xys 修复无限循环
  let isAddAffix = false;
  /**
   * @description: Set fixed tabs
   */
  function addAffixTabs(): void {
    const affixTabs = filterAffixTabs(router.getRoutes() as unknown as RouteLocationNormalized[]);
    affixList.value = affixTabs;
    for (const tab of affixTabs) {
      tabStore.addTab({
        meta: tab.meta,
        name: tab.name,
        path: tab.path,
      } as unknown as RouteLocationNormalized);
    }
  }
  // let isAddAffix = false;

  if (!isAddAffix) {
    addAffixTabs();
    isAddAffix = true;
  }
  return affixList.value.map((item) => item.meta?.title).filter(Boolean) as string[];
}

export function useTabsDrag(affixTextList: string[]) {
  // const useMultipleTabStore = useStore('useMultipleTabStore');
  const tabStore = useMultipleTabStore();
  const { multiTabsSetting } = projectSetting;
  const { prefixCls } = useDesign('multiple-tabs');
  nextTick(() => {
    if (!multiTabsSetting.canDrag) return;
    const el = document.querySelectorAll(
      `.${prefixCls} .ant-tabs-nav-wrap > div`,
    )?.[0] as HTMLElement;
    const { initSortable } = useSortable(el, {
      filter: (_evt, target: HTMLElement) => {
        const text = target.innerText;
        if (!text) return false;
        return affixTextList.map((res) => $t(res)).includes(text);
      },
      onEnd: (evt) => {
        const { oldIndex, newIndex } = evt;

        if (isNil(oldIndex) || isNil(newIndex) || oldIndex === newIndex) {
          return;
        }

        tabStore.sortTabs(oldIndex, newIndex);
      },
    });
    initSortable();
  });
}

import { ThemeEnum } from '@mxpio/enums';
import { theme } from 'ant-design-vue';
import { computed } from 'vue';

import { useRootSetting } from '../setting/useRootSetting';

export function useDarkModeTheme() {
  const { getDarkMode } = useRootSetting();
  const { darkAlgorithm } = theme;
  const isDark = computed(() => getDarkMode.value === ThemeEnum.DARK);
  const darkTheme = {
    algorithm: [darkAlgorithm],
  };

  return {
    isDark,
    darkTheme,
  };
}

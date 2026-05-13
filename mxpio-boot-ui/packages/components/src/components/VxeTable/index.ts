import 'vxe-pc-ui/lib/style.css';
import 'vxe-table/lib/style.css';
import { withInstall } from '@mxpio/utils';
import vxeBasicTable from './src/VxeBasicTable';
import { VxeTable, VxeColumn, VxeGrid, VxeToolbar } from 'vxe-table';
import VXETablePluginAntd from './src/components';
import './src/formats';
import './src/setting';
// import SelectRendererOptions from './src/components/SelectRendererOptions';
// import TreeSelectRendererOptions from './src/components/TreeSelectRendererOptions';
import { VxeUI, VxePager, VxeTooltip } from 'vxe-pc-ui';
import { i18n, registerI18nMessages } from '@mxpio/locales';
// 导入默认的语言
import zhCN from 'vxe-pc-ui/lib/language/zh-CN';
import enUS from 'vxe-table/lib/locale/lang/en-US';

export const VxeBasicTable = withInstall(vxeBasicTable);

/**
 * xys 20250904
 * 只导出外部需要用到的组件及类型
 */
// export * from 'vxe-table';
export type { VxeGridPropTypes, VxeGridInstance } from 'vxe-table';
export type { VxeFormItemProps, VxeGlobalRendererHandles } from 'vxe-pc-ui';
export type { BasicTableProps as BasicVxeTableProps } from './src/types';

VxeUI.component(VxePager);
VxeUI.component(VxeTable);
VxeUI.component(VxeColumn);
VxeUI.component(VxeGrid);
VxeUI.component(VxeToolbar);
VxeUI.component(VxeTooltip);
VxeUI.use(VXETablePluginAntd);

export function setupVxeI18n() {
  const message: Record<string, any> = {
    zh_CN: {
      ...zhCN,
    },
    en: {
      ...enUS,
    },
  };
  registerI18nMessages(message);
  VxeUI.setConfig({
    // 对组件内置的提示语进行国际化翻译
    i18n: (key, args) => i18n.global.t(key, args),
  });
}

/**
 * xys 20250926
 * 外部组件注册
 */

export { componentMap as vxeComponentMap } from './src/componentMap';
export { useVxeComponentRegister } from './src/hook/useVxeComponentRegister';
export * from './src/components/common';
export { VxeUI };
// export { SelectRendererOptions };

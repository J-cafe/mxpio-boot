// import { VXETable } from '..';
import { VxeUI } from 'vxe-pc-ui';
import setting from '@mxpio/settings';
import { usePermission } from '@mxpio/hooks/src/web/usePermission';

const componentSetting = setting.getComponentSetting();
VxeUI.setConfig(componentSetting.vxeTable);
VxeUI.setConfig({
  // 全局权限码控制方法
  permissionMethod({ code }) {
    const { hasPermission } = usePermission();
    if (hasPermission(code as string | string[])) {
      return {
        // 是否可视
        visible: true,
        // 是否禁用
        disabled: false,
      };
    }
    return {
      visible: false,
      disabled: true,
    };
  },
});

import { useVxeComponentRegister } from '@mxpio/components';
import { createAsyncComponent } from '@mxpio/utils';
import SelectDeptRenderer from './renderer/SelectDept';
import SelectUserByDeptRenderer from './renderer/SelectUserByDept';
import SelectDictRenderer from './renderer/DictSelect';
import ExportButtonRenderer from './renderer/ExportButton';
import ImportButtonRenderer from './renderer/ImportButton';

/**
 * 注册表单组件
 * 组件名称
 * 组件
/**
 * 注册表格组件
 * 组件名称
 * 组件
 * 渲染器
 */
export function vxeComponentRegister() {
  useVxeComponentRegister(
    'DeptSelect',
    createAsyncComponent(() => import('../Form/DeptSelect/index.vue')),
    SelectDeptRenderer,
  );
  useVxeComponentRegister(
    'UserByDeptSelect',
    createAsyncComponent(() => import('../Form/UserByDeptSelect/index.vue')),
    SelectUserByDeptRenderer,
  );

  useVxeComponentRegister(
    'DictSelect',
    createAsyncComponent(() => import('../Form/DictSelect.vue')),
    SelectDictRenderer,
  );
  useVxeComponentRegister(
    'ExportButton',
    createAsyncComponent(() => import('../VxeTable/components/ExportButton.vue')),
    ExportButtonRenderer,
  );
  useVxeComponentRegister(
    'ImportButton',
    createAsyncComponent(() => import('../VxeTable/components/ImportButton.vue')),
    ImportButtonRenderer,
  );
}

// export function registerComponent() {
//   FormComponentRegister();
//   vxeComponentRegister();
// }

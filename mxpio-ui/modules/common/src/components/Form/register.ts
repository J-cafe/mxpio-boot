import { useComponentRegister } from '@mxpio/components';
import { createAsyncComponent } from '@mxpio/utils';
/**
 * 注册表单组件
 * 组件名称
 * 组件
 */
export function formComponentRegister() {
  useComponentRegister(
    'DeptSelect',
    createAsyncComponent(() => import('./DeptSelect/index.vue')),
  );
  useComponentRegister(
    'UserByDeptSelect',
    createAsyncComponent(() => import('./UserByDeptSelect/index.vue')),
  );
  useComponentRegister(
    'DictSelect',
    createAsyncComponent(() => import('./DictSelect.vue')),
  );
}

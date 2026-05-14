import type DeptSelect from './src/components/form/DeptSelect/index.vue';
import type UserByDeptSelect from './src/components/form/UserByDeptSelect/index.vue';
import type DictSelect from './src/components/form/DictSelect.vue';

// 提取自定义组件的 props 类型
type DeptSelect = InstanceType<typeof DeptSelect>['$props'];
type UserByDeptSelect = InstanceType<typeof UserByDeptSelect>['$props'];
type DictSelect = InstanceType<typeof DictSelect>['$props'];
// 扩展公共组件库的 ComponentProps 接口
declare module '@mxpio/components' {
  interface ComponentProps {
    // 添加自定义组件的类型定义
    DeptSelect: DeptSelect;
    UserByDeptSelect: UserByDeptSelect;
    DictSelect: DictSelect;
  }
}

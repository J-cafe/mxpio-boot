import VFormDesign from './components/VFormDesign/index.vue';
import VFormCreate from './components/VFormCreate/index.vue';

/**
 * @description 流程相关
 * 外部注册自定义表单
 */
import CustomForm from './examples/CustomForm.vue';
import { registerCustomForms } from './core/formItemConfig';

export * from './core/formItemConfig';
export { VFormDesign, VFormCreate };

registerCustomForms([
  {
    key: 'CustomForm',
    label: '自定义表单1',
    component: CustomForm,
  },
]);

/**
 * @description 流程相关
 * 注册表单设计器自定义组件
 */
// export function vFormDesignRegister() {
//   const config = [
//     {
//       component: 'UserByDeptSelect',
//       label: '选择用户',
//       icon: 'ant-design:user-outlined',
//       field: '',
//       colProps: { span: 24 },
//       componentProps: {},
//     },
//   ];

//   const attrs = {
//     DictSelect: [
//       {
//         name: 'dictCode',
//         label: '字典编码',
//         component: 'ApiSelectPage',
//         componentProps: {
//           url: '/sys/dict/tree/page',
//           optionLabel: 'dictName',
//           optionValue: 'dictCode',
//           showSearch: true,
//         },
//       },
//       {
//         name: 'numberToString',
//         label: '数字转字符串',
//         component: 'Checkbox',
//       },
//     ],
//   };

//   registerCustomComponents(config, attrs);
// }

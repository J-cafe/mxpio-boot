import { registerCustomComponents } from '@mxpio/components';

// 注册表单设计器自定义组件
export function vFormDesignRegister() {
  const config = [
    {
      component: 'UserByDeptSelect',
      label: '用户选择',
      icon: 'ant-design:user-outlined',
      field: '',
      colProps: { span: 24 },
      componentProps: {},
    },
    {
      component: 'DictSelect',
      label: '字典选择',
      icon: 'ant-design:account-book-filled',
      field: '',
      colProps: { span: 24 },
      componentProps: {},
    },
    {
      component: 'DeptSelect',
      label: '选择部门',
      icon: 'ant-design:deployment-unit-outlined',
      field: '',
      colProps: { span: 24 },
      componentProps: {},
    },
  ];

  const attrs = {
    DictSelect: [
      {
        name: 'dictCode',
        label: '字典编码',
        component: 'ApiSelectPage',
        componentProps: {
          url: '/sys/dict/tree/page',
          optionLabel: 'dictName',
          optionValue: 'dictCode',
          showSearch: true,
        },
      },
      {
        name: 'numberToString',
        label: '数字转字符串',
        component: 'Checkbox',
      },
    ],
    UserByDeptSelect: [
      {
        name: 'multiple',
        label: '多选',
        component: 'Checkbox',
        componentProps: {
          multiple: false,
        },
      },
    ],
    DeptSelect: [
      {
        name: 'multiple',
        label: '多选',
        component: 'Checkbox',
      },
    ],
    ApiSelectPage: [
      {
        name: 'url',
        label: '接口',
        component: 'Input',
      },
      {
        name: 'optionLabel',
        label: '显示名称',
        component: 'Input',
      },
      {
        name: 'optionValue',
        label: '实际值',
        component: 'Input',
      },
    ],
  };

  registerCustomComponents(config, attrs);
}

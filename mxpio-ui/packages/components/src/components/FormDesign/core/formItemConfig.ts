/**
 * @description：表单配置
 */
import type { IBaseComponentProps } from '../typings/v-form-component';
import { IVFormComponent, ICustomForm } from '../typings/v-form-component';
import { isArray } from 'lodash-es';
import formDesignComponents from './components';

// import { componentMap as VbenCmp, add } from '../../Form';
// import type { ComponentType } from '../../Form';
// import { componentMap as Cmp } from '../components';
// import { Component } from 'vue';

/**
 * 外部业务自定义表单
 * 20260209 xys 调整为单独的类来实现组件
 */
// const componentFDMap = new Map<string, Component>();

//如果有其它控件，可以在这里初始化

// //注册Ant控件库
// Cmp.forEach((value, key) => {
//   componentFDMap.set(key, value);
//   if (VbenCmp[key] == null) {
//     add(key as ComponentType, value);
//   }
// });

// //注册vben控件库
// VbenCmp.forEach((value, key) => {
//   componentFDMap.set(key, value);
// });

const customFormsMap = new Map<string, ICustomForm>();
function registerCustomForms(options: ICustomForm[]) {
  options.forEach((item) => {
    customFormsMap.set(item.key, item);
  });
}

export { customFormsMap, registerCustomForms };

/**
 * 设置自定义表单控件
 * @param {IVFormComponent | IVFormComponent[]} config
 */
export function setFormDesignComponents(config: IVFormComponent | IVFormComponent[]) {
  if (isArray(config)) {
    config.forEach((item) => {
      const { componentInstance: component, ...rest } = item;
      // componentFDMap[item.component] = component;
      component && formDesignComponents.setComponent(item.component, component);
      customComponents.push(Object.assign({ props: {} }, rest));
    });
  } else {
    const { componentInstance: component, ...rest } = config;
    // componentFDMap[config.component] = component;
    component && formDesignComponents.setComponent(config.component, component);
    customComponents.push(Object.assign({ props: {} }, rest));
  }
}

/**
 * 注册自定义表单组件到表单设计器
 * @param config 自定义组件配置项或配置项数组
 * @description 该函数用于将用户自定义的表单组件注册到表单设计器中，
 * 支持批量注册多个组件或单个组件，注册后组件将显示在表单设计器的组件面板中
 */

export function registerCustomComponents(
  config: IVFormComponent | IVFormComponent[],
  attrs?: IBaseComponentProps,
) {
  if (isArray(config)) {
    config.forEach((item) => {
      const { componentInstance: component, ...rest } = item;
      component && formDesignComponents.setComponent(item.component, component);
      formDesignComponents.addCustomComponents(Object.assign({ props: {} }, rest));
      // customComponents.push(Object.assign({ props: {} }, rest));
    });
  } else {
    const { componentInstance: component, ...rest } = config;
    component && formDesignComponents.setComponent(config.component, component);
    formDesignComponents.addCustomComponents(Object.assign({ props: {} }, rest));
    // customComponents.push(Object.assign({ props: {} }, rest));
  }

  attrs && formDesignComponents.addCustomAttrs(attrs);
}

//外部设置的自定义控件
export const customComponents: IVFormComponent[] = [
  // {
  //   component: 'CustomForm',
  //   label: '自定义表单',
  //   icon: 'line-md:iconify2',
  //   colProps: { span: 24 },
  //   field: '',
  //   componentProps: {},
  // },
];

// 左侧控件列表与初始化的控件属性
// props.slotName,会在formitem级别生成一个slot,并绑定当前record值
// 属性props，类型为对象，不能为undefined或是null。
export const baseComponents: IVFormComponent[] = [
  {
    component: 'InputCountDown',
    label: '倒计时输入',
    icon: 'line-md:iconify2',
    colProps: { span: 24 },
    field: '',
    componentProps: {},
  },
  {
    component: 'IconPicker',
    label: '图标选择器',
    icon: 'line-md:iconify2',
    colProps: { span: 24 },
    field: '',
    componentProps: {},
  },
  {
    component: 'StrengthMeter',
    label: '密码强度',
    icon: 'wpf:password1',
    colProps: { span: 24 },
    field: '',
    componentProps: {},
  },
  {
    component: 'AutoComplete',
    label: '自动完成',
    icon: 'wpf:password1',
    colProps: { span: 24 },
    field: '',
    componentProps: {
      placeholder: '请输入正则表达式',
      options: [
        {
          value: '/^(?:(?:\\+|00)86)?1[3-9]\\d{9}$/',
          label: '手机号码',
        },
        {
          value: '/^((ht|f)tps?:\\/\\/)?[\\w-]+(\\.[\\w-]+)+:\\d{1,5}\\/?$/',
          label: '网址带端口号',
        },
      ],
    },
  },
  {
    component: 'Divider',
    label: '分割线',
    icon: 'radix-icons:divider-horizontal',
    colProps: { span: 24 },
    field: '',
    componentProps: {
      orientation: 'center',
      dashed: true,
    },
  },
  {
    component: 'Checkbox',
    label: '复选框',
    icon: 'ant-design:check-circle-outlined',
    colProps: { span: 24 },
    field: '',
  },
  {
    component: 'CheckboxGroup',
    label: '复选框-组',
    icon: 'ant-design:check-circle-filled',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    component: 'Input',
    label: '输入框',
    icon: 'bi:input-cursor-text',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      type: 'text',
    },
  },
  {
    component: 'InputNumber',
    label: '数字输入框',
    icon: 'ant-design:field-number-outlined',
    field: '',
    colProps: { span: 24 },
    componentProps: { style: 'width:200px' },
  },
  {
    component: 'InputTextArea',
    label: '文本域',
    icon: 'ant-design:file-text-filled',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'Select',
    label: '下拉选择',
    icon: 'gg:select',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },

  {
    component: 'Radio',
    label: '单选框',
    icon: 'ant-design:check-circle-outlined',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'RadioGroup',
    label: '单选框-组',
    icon: 'carbon:radio-button-checked',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    component: 'DatePicker',
    label: '日期选择',
    icon: 'healthicons:i-schedule-school-date-time-outline',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'RangePicker',
    label: '日期范围',
    icon: 'healthicons:i-schedule-school-date-time-outline',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      placeholder: ['开始日期', '结束日期'],
    },
  },
  {
    component: 'MonthPicker',
    label: '月份选择',
    icon: 'healthicons:i-schedule-school-date-time-outline',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      placeholder: '请选择月份',
    },
  },
  {
    component: 'TimePicker',
    label: '时间选择',
    icon: 'healthicons:i-schedule-school-date-time',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'Slider',
    label: '滑动输入条',
    icon: 'vaadin:slider',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'Rate',
    label: '评分',
    icon: 'ic:outline-star-rate',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'Switch',
    label: '开关',
    icon: 'entypo:switch',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
  {
    component: 'TreeSelect',
    label: '树形选择',
    icon: 'clarity:tree-view-line',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      treeData: [
        {
          label: '选项1',
          value: '1',
          children: [
            {
              label: '选项三',
              value: '1-1',
            },
          ],
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    component: 'Upload',
    label: '上传',
    icon: 'ant-design:upload-outlined',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      api: () => 1,
    },
  },
  {
    component: 'Cascader',
    label: '级联选择',
    icon: 'ant-design:check-outlined',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
          children: [
            {
              label: '选项三',
              value: '1-1',
            },
          ],
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  // {
  //   component: 'Button',
  //   label: '按钮',
  //   icon: 'dashicons:button',
  //   field: '',
  //   colProps: { span: 24 },
  //   hiddenLabel: true,
  //   componentProps: {},
  // },
  // {
  //   component: 'ColorPicker',
  //   label: '颜色选择器',
  //   icon: 'carbon:color-palette',
  //   field: '',
  //   colProps: { span: 24 },
  //   componentProps: {
  //     defaultValue: '',
  //     value: '',
  //   },
  // },

  {
    component: 'slot',
    label: '插槽',
    icon: 'vs:timeslot-question',
    field: '',
    colProps: { span: 24 },
    componentProps: {
      slotName: 'slotName',
    },
  },
  // {
  //   component: 'DictSelect',
  //   label: '字典选择',
  //   icon: 'ant-design:account-book-filled',
  //   field: '',
  //   colProps: { span: 24 },
  //   componentProps: {},
  // },
  // {
  //   component: 'DeptSelect',
  //   label: '选择部门',
  //   icon: 'ant-design:deployment-unit-outlined',
  //   field: '',
  //   colProps: { span: 24 },
  //   componentProps: {},
  // },
  // {
  //   component: 'UserByDeptSelect',
  //   label: '选择用户',
  //   icon: 'ant-design:user-outlined',
  //   field: '',
  //   colProps: { span: 24 },
  //   componentProps: {},
  // },
  {
    component: 'ApiSelectPage',
    label: '分页选择',
    icon: 'ant-design:user-outlined',
    field: '',
    colProps: { span: 24 },
    componentProps: {},
  },
];

// https://next.antdv.com/components/transfer-cn
const transferControl = {
  component: 'Transfer',
  label: '穿梭框',
  icon: 'bx:bx-transfer-alt',
  field: '',
  colProps: { span: 24 },
  componentProps: {
    render: (item) => item.title,
    dataSource: [
      {
        key: 'key-1',
        title: '标题1',
        description: '描述',
        disabled: false,
        chosen: true,
      },
      {
        key: 'key-2',
        title: 'title2',
        description: 'description2',
        disabled: true,
      },
      {
        key: 'key-3',
        title: '标题3',
        description: '描述3',
        disabled: false,
        chosen: true,
      },
    ],
  },
};

baseComponents.push(transferControl);

export const layoutComponents: IVFormComponent[] = [
  {
    field: '',
    component: 'Grid',
    label: '栅格布局',
    icon: 'icon-grid',
    componentProps: {},
    columns: [
      {
        span: 12,
        children: [],
      },
      {
        span: 12,
        children: [],
      },
    ],
    colProps: { span: 24 },
    options: {
      gutter: 0,
    },
  },
];

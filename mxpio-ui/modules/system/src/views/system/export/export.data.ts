import { BasicColumn, FormSchema } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '方案编码',
    dataIndex: 'code',
    width: 160,
    align: 'left',
  },
  {
    title: '文件名称',
    dataIndex: 'fileName',
  },

  {
    title: '接口',
    dataIndex: 'api',
  },
];

export const itemColumns: BasicColumn[] = [
  {
    title: '字段名称',
    dataIndex: 'columnName',
  },
  {
    title: '标签',
    dataIndex: 'label',
  },
  {
    title: '级别',
    dataIndex: 'level',
  },
  {
    title: '宽度',
    dataIndex: 'width',
  },
  {
    title: '顺序',
    dataIndex: 'sort',
  },
  {
    title: '数据对齐方式',
    dataIndex: 'dataAlign',
  },
];

export const itemFormSchema: FormSchema[] = [
  {
    field: 'columnName',
    label: '字段名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'label',
    label: '标签',
    component: 'Input',
    required: true,
  },
  {
    field: 'level',
    label: '级别',
    component: 'Input',
    required: true,
  },
  {
    field: 'width',
    label: '宽度',
    component: 'InputNumber',
    rules: [
      {
        type: 'number',
        min: 0,
        message: '请输入大于等于1的数字',
      },
    ],
    required: true,
  },
  {
    field: 'sort',
    label: '顺序',
    component: 'InputNumber',
    rules: [
      {
        type: 'number',
        min: 0,
        message: '请输入大于等于1的数字',
      },
    ],
    required: true,
  },
  {
    field: 'dataAlign',
    label: '数据对齐方式',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        {
          label: '左对齐',
          value: 0,
        },
        {
          label: '居中',
          value: 1,
        },
        {
          label: '右对齐',
          value: 2,
        },
      ],
    },
  },
];

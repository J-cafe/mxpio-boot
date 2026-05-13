import { BasicColumn, FormSchema } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '字典编码',
    dataIndex: 'dictCode',
    width: 220,
    align: 'left',
    sorter: true,
  },
  {
    title: '字典名称',
    dataIndex: 'dictName',
  },
  {
    title: '字典类别',
    dataIndex: 'dictType',
    width: 120,
    customRender: ({ record }) => {
      const dictType = record.dictType;
      let text = '';
      switch (dictType) {
        case '1':
          text = '系统字典';
          break;
        case '2':
          text = '业务字典';
          break;
        default:
          break;
      }
      return text;
    },
  },
  {
    title: '默认值',
    dataIndex: 'dictDefaultValue',
  },
  {
    title: '描述',
    dataIndex: 'dictDesc',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'dictCode',
    label: '字典编码',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'dictName',
    label: '字典名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'dictType@EQ',
    label: '字典类别',
    component: 'Select',
    colProps: { span: 8 },
    componentProps: {
      options: [
        {
          label: '系统字典',
          value: '1',
        },
        {
          label: '业务字典',
          value: '2',
        },
      ],
    },
  },
];

export const dictItemFormSchema: FormSchema[] = [
  {
    field: 'itemText',
    label: '名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'itemValue',
    label: '数据值',
    component: 'Input',
    required: true,
  },
  {
    field: 'itemSort',
    label: '排序值',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'itemStatus',
    label: '是否启用',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        {
          label: '启用',
          value: '1',
        },
        {
          label: '停用',
          value: '0',
        },
      ],
    },
    defaultValue: '1',
  },
  {
    field: 'itemDesc',
    label: '描述',
    component: 'InputTextArea',
  },
];

export const dictItemColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'itemText',
  },
  {
    title: '数据值',
    dataIndex: 'itemValue',
  },
  {
    title: '描述',
    dataIndex: 'itemDesc',
  },
];

import { BasicColumn, FormSchema, VxeGridPropTypes } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '方案编码',
    dataIndex: 'code',
    align: 'left',
    width: 100,
  },
  {
    title: '方案名称',
    dataIndex: 'name',
    width: 100,
  },
  {
    title: '数据源',
    dataIndex: 'entityManagerFactoryName',
  },
  {
    title: '实体类',
    dataIndex: 'entityClassName',
  },
  {
    title: 'Sheet页',
    dataIndex: 'excelSheetName',
    width: 100,
  },
];

export const itemColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'name',
  },
  {
    title: 'Excel列号',
    dataIndex: 'excelColumn',
  },
  {
    title: '实体属性',
    dataIndex: 'propertyName',
  },
  {
    title: '前置',
    children: [
      {
        title: '解析器',
        dataIndex: 'cellPreParserBean',
      },
      {
        title: '参数',
        dataIndex: 'cellPreParserParam',
      },
    ],
  },
  {
    title: '后置',
    children: [
      {
        title: '解析器',
        dataIndex: 'cellPostParserBean',
      },
      {
        title: '参数',
        dataIndex: 'cellPostParserParam',
      },
    ],
  },
];

export const itemFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'excelColumn',
    label: 'Excel列号',
    component: 'InputNumber',
    required: true,
  },
  {
    field: 'propertyName',
    label: '实体属性',
    component: 'Input',
    required: true,
  },
  {
    field: 'cellPreParserBean',
    label: '前置解析器',
    component: 'ApiSelect',
    componentProps: {
      showSearch: true,
    },
  },
  {
    field: 'cellPreParserParam',
    label: '前置参数',
    component: 'InputTextArea',
  },
  {
    field: 'cellPostParserBean',
    label: '后置解析器',
    component: 'ApiSelect',
    componentProps: {
      showSearch: true,
    },
  },
  {
    field: 'cellPostParserParam',
    label: '后置参数',
    component: 'InputTextArea',
  },
];

export const vxeTableColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '关键字',
    field: 'key',
    editRender: {
      name: 'AInput',
      placeholder: '请点击输入',
    },
  },
  {
    title: '值',
    field: 'value',
    editRender: {
      name: 'AInput',
      placeholder: '请点击输入',
    },
  },
];

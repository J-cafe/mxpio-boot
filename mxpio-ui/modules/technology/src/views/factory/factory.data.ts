import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '工厂编码',
    field: 'factoryCode',
    width: 120,
    sortable: true,
  },
  {
    title: '工厂名称',
    field: 'factoryName',
  },
  {
    title: '工厂简称',
    field: 'factoryShortName',
  },
  {
    title: '工厂类型',
    field: 'factoryType',
    formatter: 'dictText',
  },
  {
    title: '是否启用',
    field: 'status',
    formatter: 'dictText',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'factoryCode',
    title: '工厂编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'factoryName',
    title: '工厂名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'factoryType@EQ',
    title: '工厂类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_FACTORY_TYPE',
      },
    },
    span: 6,
  },
  {
    span: 6,
    className: '!pr-0',
    itemRender: {
      name: 'AButtonGroup',
      children: [
        {
          props: { type: 'primary', content: '查询', htmlType: 'submit' },
          attrs: { class: 'mr-2' },
        },
        { props: { type: 'default', htmlType: 'reset', content: '重置' } },
      ],
    },
  },
];

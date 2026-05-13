import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '工作中心编码',
    field: 'workCenterCode',
    width: 140,
    sortable: true,
  },
  {
    title: '名称',
    field: 'workCenterName',
  },
  {
    title: '简称',
    field: 'workCenterShortName',
  },
  {
    title: '工作中心类型',
    field: 'workCenterType',
    formatter: 'dictText',
  },
  {
    title: '所属车间',
    field: 'workShopCode',
    formatter: 'dictText',
  },
  {
    title: '备注',
    field: 'memo',
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
    field: 'workCenterCode',
    title: '编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workCenterName',
    title: '名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workShopCode@EQ',
    title: '所属车间',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_WORK_SHOP_TYPE',
      },
      attrs: { class: '!w-full' },
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

import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '车间编码',
    field: 'workShopCode',
    width: 120,
    sortable: true,
  },
  {
    title: '车间名称',
    field: 'workShopName',
  },
  {
    title: '车间简称',
    field: 'workShopShortName',
  },
  {
    title: '车间类型',
    field: 'workShopType',
    formatter: 'dictText',
  },
  {
    title: '加工模式',
    field: 'procMode',
    formatter: 'dictText',
  },
  {
    title: '物料组',
    field: 'itemGroupCode',
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
    field: 'workShopCode',
    title: '车间编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workShopName',
    title: '车间名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workShopType@EQ',
    title: '车间类型',
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
    field: 'procMode@EQ',
    title: '加工模式',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_MES_PROC_MODE',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    span: 6,
    className: '!pr-0',
    collapseNode: true,
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

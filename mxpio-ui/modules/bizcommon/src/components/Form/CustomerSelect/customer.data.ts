import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '客户编码',
    field: 'pnCode',
    width: 120,
    sortable: true,
  },
  {
    title: '客户名称',
    field: 'pnName',
  },
  {
    title: '简称',
    field: 'pnAbbr',
  },
  {
    title: '联系人',
    field: 'pnContacts',
  },
  {
    title: '电话',
    field: 'pnTel',
  },
  {
    title: '业务员',
    field: 'bizMan',
    formatter: 'dictText',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'pnCode',
    title: '客户编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'pnName',
    title: '客户名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'pnAbbr',
    title: '客户简称',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    span: 8,
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

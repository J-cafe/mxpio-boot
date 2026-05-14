import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

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
  { title: '评价内容', field: 'content' },
  { title: '评价类型', field: 'type', formatter: 'dictText', sortable: true },
  { title: '排序', field: 'sortId', formatter: 'dictText', sortable: true },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'content',
    title: '评价内容',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'type@EQ',
    title: '评价类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_ERP_EQUIPMENT_EVALUATE_TYPE',
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

import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '分类名称', field: 'name', treeNode: true },
  { title: '分类编码', field: 'code' },
  { title: '是否启用', field: 'isEnable', formatter: 'dictText' },
  { title: '备注', field: 'remarks' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'name',
    title: '分类名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'code',
    title: '分类编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'isEnable@EQ',
    title: '是否启用',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_YESNO',
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

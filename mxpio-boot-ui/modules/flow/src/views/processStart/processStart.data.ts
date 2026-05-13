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
    title: '流程编码',
    field: 'code',
    width: 160,
    align: 'left',
    sortable: true,
  },
  {
    title: '流程名称',
    field: 'name',
  },
  {
    title: '当前版本',
    field: 'lastDefVersion',
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
  },
  {
    title: '创建部门',
    field: 'createDept',
    formatter: 'dictText',
  },
  {
    title: '创建时间',
    field: 'createTime',
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
    field: 'code',
    title: '流程编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'name',
    title: '流程名称',
    itemRender: {
      name: 'AInput',
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

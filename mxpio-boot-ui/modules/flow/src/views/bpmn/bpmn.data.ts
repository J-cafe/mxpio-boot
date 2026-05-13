import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

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
    sortable: true,
  },
  {
    title: '流程名称',
    field: 'name',
  },
  {
    title: '发布状态',
    field: 'status',
    formatter: 'dictText',
  },
  {
    title: '当前key',
    field: 'lastDefId',
  },
  {
    title: '当前版本',
    field: 'lastDefVersion',
  },
  {
    title: '是否显示',
    field: 'visible',
    slots: {
      default: ({ row }) => {
        const status = row.visible;
        const enable = status === true;
        const color = enable ? 'green' : 'red';
        const text = enable ? '是' : '否';
        return h(Tag, { color: color }, () => text);
      },
    },
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
    field: 'status@EQ',
    title: '发布状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_FLOW_DEPLOY_STATUS',
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

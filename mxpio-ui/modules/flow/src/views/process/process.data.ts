import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '流程标题',
    field: 'title',
    width: 160,
    align: 'left',
  },
  {
    title: '流程编码',
    field: 'processDefinitionKey',
  },
  {
    title: '发布状态',
    field: 'processDefinitionName',
  },
  {
    title: '流程版本',
    field: 'processVersion',
  },
  {
    title: '开始时间',
    field: 'startTime',
  },
  {
    title: '审批状态',
    field: 'state',
    slots: {
      default: ({ row }) => {
        const state = row.state;
        let color = '';
        let text = '';
        if (state === 'ACTIVE') {
          color = 'orange';
          text = '审批中';
        } else if (state === 'SUSPENDED') {
          color = '#f50';
          text = '已暂停';
        } else if (state === 'COMPLETED') {
          color = 'green';
          text = '已完成';
        } else if (state === 'INTERNALLY_TERMINATED') {
          color = 'red';
          text = '已终止';
        }

        return h(Tag, { color: color }, () => text);
      },
    },
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
    field: 'title',
    title: '流程标题',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'processDefinitionKey',
    title: '流程编码',
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

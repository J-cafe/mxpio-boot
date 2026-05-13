import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import { dateUtil } from '@mxpio/utils';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '流程标题',
    field: 'title',
    width: 160,
    align: 'left',
    sortable: true,
  },
  {
    title: '流程名称',
    field: 'processDefinitionName',
  },
  {
    title: '流程编码',
    field: 'processDefinitionKey',
  },
  {
    title: '流程版本',
    field: 'processVersion',
  },
  {
    title: '开始时间',
    field: 'startTime',
    slots: {
      default: ({ row }) => {
        return row.startTime ? dateUtil(row.startTime).format('YYYY-MM-DD HH:mm:ss') : '';
      },
    },
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
    width: 150,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: '$BPMN_TITLE_',
    title: '流程标题',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'processDefinitionName',
    title: '流程名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'state@EQ',
    title: '流程状态',
    itemRender: {
      name: 'ASelect',
      props: {
        options: [
          { label: '审批中', value: 'ACTIVE' },
          { label: '已暂停', value: 'SUSPENDED' },
          { label: '已完成', value: 'COMPLETED' },
          { label: '已终止', value: 'INTERNALLY_TERMINATED' },
        ],
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

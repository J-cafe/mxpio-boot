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
    title: '流程标题',
    field: 'title',
    width: 160,
    sortable: true,
  },
  {
    title: '流程名称',
    field: 'processDefinitionName',
  },
  {
    title: '任务类型',
    field: 'taskType',
    formatter: 'dictText',
    slots: {
      default: ({ row }) => {
        const taskType = row.taskType;
        let color = '';
        let text = '';
        if (taskType === 'active') {
          color = 'green';
          text = '待办任务';
        } else if (taskType === 'candidateUser') {
          color = '#108ee9';
          text = '候选任务';
        } else if (taskType === 'candidateGroup') {
          color = '#2db7f5';
          text = '组任务';
        }

        return h(Tag, { color: color }, () => text);
      },
    },
  },
  {
    title: '紧急级别',
    field: 'bpmnSortFlag',
    formatter: 'dictText',
    slots: {
      default: ({ row }) => {
        const status = row.bpmnSortFlag;
        const enable = status === '0';
        const color = enable ? '#2db7f5' : 'red';
        const text = enable ? '普通' : '紧急';
        return h(Tag, { color: color }, () => text);
      },
    },
    sortable: true,
  },
  {
    title: '当前审批人',
    field: 'assignee',
    formatter: 'dictText',
  },
  {
    title: '节点名称',
    field: 'name',
  },
  {
    title: '流程发起人',
    field: 'procStartUserId',
    formatter: 'dictText',
  },
  {
    title: '审批开始时间',
    field: 'createTime',
  },
  {
    title: '流程发起时间',
    field: 'procStartTime',
    sortable: true,
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
    field: 'procTitle',
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
    field: 'procStartUserId@IN',
    title: '发起人',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bpmnSortFlag@EQ',
    title: '是否加急',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'bizType@EQ',
    title: '业务分类',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_BPMN_BIZ_TYPE',
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

export const finishedColumns: VxeGridPropTypes.Columns = [
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
    sortable: true,
  },
  {
    title: '流程名称',
    field: 'processDefinitionName',
  },
  {
    title: '当前审批人',
    field: 'assignee',
    formatter: 'dictText',
  },
  {
    title: '节点名称',
    field: 'name',
  },
  {
    title: '流程发起人',
    field: 'procStartUserId',
    formatter: 'dictText',
  },
  {
    title: '审批开始时间',
    field: 'procStartTime',
  },
  {
    title: '节点办理时间',
    field: 'endTime',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFinishedFormSchema: VxeFormItemProps[] = [
  {
    field: '$BPMN_TITLE_',
    title: '流程标题',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'processDefinitionName@EQ',
    title: '流程名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'procStartUserId',
    title: '发起人',
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

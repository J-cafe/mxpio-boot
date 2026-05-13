import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '审核单号', field: 'id' },
  { title: '变更事由', field: 'changeReason' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '审核意见', field: 'reviewOpinion' },
  { title: '计划编码', field: 'code' },
  { title: '计划名称', field: 'name' },
  { title: '计划类别', field: 'type', formatter: 'dictText' },
  { title: '滚动周期', field: 'rollPeriod' },
  { title: '开始时间', field: 'startDate' },
  { title: '结束时间', field: 'endDate' },
  { title: '变更人', field: 'createBy', formatter: 'dictText' },
  { title: '变更时间', field: 'createTime' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'code',
    title: '计划编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'name',
    title: '计划名称',
    itemRender: {
      name: 'AInput',
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

export const formSchema: FormSchema[] = [
  {
    field: 'bizNo',
    label: '单据编码',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'demandType',
    label: '需求类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_SALES_DEMAND_TYPE',
        disabled: true,
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'bpmnStatus',
    label: '审核状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_BPMN_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderStatus',
    label: '执行状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_ORDER_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'closeStatus',
    label: '关闭状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_CLOSE_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'demandOrg',
    label: '需求组织',
    required: true,
    component: 'DeptSelect',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'usage',
    label: '用途',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_SALES_DEMAND_USAGE',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
];

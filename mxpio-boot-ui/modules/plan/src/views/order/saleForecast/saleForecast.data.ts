import { VxeGridPropTypes, FormSchema } from '@mxpio/components';

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
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  {
    title: '预测单号',
    field: 'bizNo',
    width: 140,
    sortable: true,
    filterRender: {
      name: 'AInput',
    },
    filters: [{ data: '' }],
  },
  {
    title: '预测类型',
    field: 'predictionType',
    formatter: 'dictText',
    width: 140,
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_PREDICTION_TYPE',
        mode: 'multiple',
        operator: 'IN',
      },
    },
    filters: [{ data: [] }],
  },
  {
    title: '审批状态',
    field: 'bpmnStatus',
    width: 140,
    formatter: 'dictText',
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
        mode: 'multiple',
        operator: 'IN',
      },
    },
    filters: [{ data: [] }],
  },
  {
    title: '关闭状态',
    field: 'closeStatus',
    width: 140,
    formatter: 'dictText',
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
        operator: 'EQ',
      },
    },
    filters: [{ data: [] }],
  },
  {
    title: '预测组织',
    field: 'predictionOrg',
    width: 140,
    formatter: 'dictText',
    filterRender: {
      name: 'DeptSelect',
    },
    filters: [{ data: '' }],
  },
  {
    title: '申请人',
    field: 'applyMan',
    filterRender: {
      name: 'UserByDeptSelect',
      props: {
        operator: 'IN',
      },
    },
    filters: [{ data: '' }],
    formatter: 'dictText',
    width: 140,
  },
  {
    title: '创建时间',
    field: 'createTime',
    filterRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
    },
    filters: [{ data: [] }],
    width: 140,
  },
  {
    title: '审核人',
    field: 'reviewer',
    formatter: 'dictText',
    width: 140,
    filterRender: {
      name: 'UserByDeptSelect',
      props: {
        operator: 'IN',
      },
    },
    filters: [{ data: '' }],
  },
  {
    title: '审核时间',
    field: 'reviewTime',
    width: 140,
    filterRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
    },
    filters: [{ data: [] }],
  },
  {
    title: '预测说明',
    field: 'memo',
    width: 140,
    filterRender: {
      name: 'AInput',
    },
    filters: [{ data: '' }],
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 120,
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'bizNo',
    label: '预测单号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'predictionType',
    label: '预测类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_SALES_PREDICTION_TYPE',
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
    field: 'predictionOrg',
    label: '预测组织',
    component: 'DeptSelect',
    required: true,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'applyMan',
    label: '申请人',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    required: true,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'memo',
    label: '预测说明',
    component: 'InputTextArea',
    colProps: {
      span: 6,
    },
  },
];

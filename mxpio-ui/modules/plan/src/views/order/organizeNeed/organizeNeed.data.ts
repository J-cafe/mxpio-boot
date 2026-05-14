import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

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
  { title: '需求单号', field: 'bizNo', sortable: true },
  { title: '需求类型', field: 'demandType', formatter: 'dictText' },
  { title: '审批状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '执行状态', field: 'orderStatus', formatter: 'dictText', width: 100 },
  { title: '关闭状态', field: 'closeStatus', formatter: 'dictText', width: 100 },
  { title: '需求组织', field: 'demandOrg', formatter: 'dictText' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建时间', field: 'createTime' },
  { title: '审核人', field: 'reviewer', formatter: 'dictText' },
  { title: '审核时间', field: 'reviewTime' },
  { title: '备注', field: 'memo' },
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
    field: 'bizNo',
    title: '需求单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bpmnStatus@EQ',
    title: '审核状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'createBy@EQ',
    title: '创建人',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    span: 6,
  },
  {
    field: 'orderStatus@EQ',
    title: '执行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'orderStatus@EQ',
    title: '执行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'closeStatus@EQ',
    title: '关闭状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'demandType@EQ',
    title: '需求类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_DEMAND_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'demandOrg@EQ',
    title: '需求组织',
    itemRender: {
      name: 'DeptSelect',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'createTime@IN',
    title: '创建日期',
    folding: true,
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
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
  },
];

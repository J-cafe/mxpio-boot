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
  { title: '供应商编码', field: 'pnCode', width: 120 },
  { title: '供应商名称', field: 'pnName', width: 120 },
  {
    title: '单据编号',
    field: 'bizNo',
    width: 140,
    sortable: true,
  },
  {
    title: '单据日期',
    field: 'orderDate',
  },
  {
    title: '单据类型',
    field: 'orderType',
    formatter: 'dictText',
  },
  {
    title: '审批状态',
    field: 'bpmnStatus',
    formatter: 'dictText',
  },
  {
    title: '执行状态',
    field: 'orderStatus',
    formatter: 'dictText',
  },
  {
    title: '关闭状态',
    field: 'closeStatus',
    formatter: 'dictText',
  },
  { title: '业务员', field: 'bizMan', width: 100, formatter: 'dictText' },
  {
    title: '制单时间',
    field: 'createTime',
  },
  {
    title: '审核人',
    field: 'reviewer',
    formatter: 'dictText',
  },
  {
    title: '审核时间',
    field: 'reviewTime',
  },
  {
    title: '备注',
    field: 'memo',
  },
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
    title: '单据编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'pnName',
    title: '供应商',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bizMan@EQ',
    title: '业务员',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    span: 6,
  },
  {
    field: 'orderDate@EQ',
    title: '单据日期',
    folding: true,
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'orderType@EQ',
    title: '单据类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_PURC_ORDER_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'bpmnStatus',
    title: '审核状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
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
    field: 'orderType',
    label: '单据类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_PURC_ORDER_TYPE',
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderDate',
    label: '单据日期',
    required: true,
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
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
    field: 'pnCode',
    label: '供应商',
    component: 'SupplySelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (pnCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            pnCode: pnCode || '',
            pnName: rows?.pnName || '',
            pnAbbr: rows?.pnAbbr || '',
            bizMan: rows?.bizMan || '',
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'pnName',
    label: '供应商名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'pnAbbr',
    label: '简称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'bizMan',
    label: '业务员',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'contractNo',
    label: '合同编号',
    component: 'Input',
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

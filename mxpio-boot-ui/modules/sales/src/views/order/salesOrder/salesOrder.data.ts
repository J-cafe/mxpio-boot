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
  {
    title: '单据编号',
    field: 'bizNo',
    width: 140,
    sortable: true,
    filterRender: {
      name: 'AInput',
    },
    filters: [{ data: '' }],
  },
  {
    title: '单据日期',
    field: 'orderDate',
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
    title: '单据类型',
    field: 'orderType',
    formatter: 'dictText',
    width: 140,
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_ORDER_TYPE',
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
    title: '执行状态',
    field: 'orderStatus',
    width: 140,
    formatter: 'dictText',
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
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
    title: '业务类型',
    field: 'bizType',
    width: 140,
    formatter: 'dictText',
    filterRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_BIZ_TYPE',
        mode: 'multiple',
        operator: 'IN',
      },
    },
    filters: [{ data: [] }],
  },
  {
    title: '客户编码',
    width: 140,
    field: 'pnCode',
    filterRender: {
      name: 'AInput',
    },
    filters: [{ data: '' }],
  },
  {
    title: '客户名称',
    width: 140,
    field: 'pnName',
    filterRender: {
      name: 'AInput',
    },
    filters: [{ data: '' }],
  },
  {
    title: '业务员',
    field: 'bizMan',
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
    title: '制单时间',
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
    title: '备注',
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
    field: 'bpmnStatus@IN',
    title: '审核状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
        mode: 'multiple',
      },
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
    field: 'orderType@EQ',
    title: '单据类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_ORDER_TYPE',
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
    field: 'pnName',
    title: '客户名称',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'bizType@EQ',
    title: '业务类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_BIZ_TYPE',
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
        dictCode: 'ERP_SALES_ORDER_TYPE',
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
    field: 'bizType',
    label: '业务类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_SALES_BIZ_TYPE',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'pnCode',
    label: '客户编码',
    component: 'CustomerSelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (pnCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
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
    label: '客户名称',
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
    label: '客户简称',
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
    field: 'currency',
    label: '币种',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_COMMON_CURRENCY',
      };
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

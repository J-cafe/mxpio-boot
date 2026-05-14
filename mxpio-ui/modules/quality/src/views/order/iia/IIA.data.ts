import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  { title: '单据编码', field: 'bizNo' },
  { title: '单据状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '执行状态', field: 'orderStatus', formatter: 'dictText' },
  { title: '仓库名称', field: 'whCode', formatter: 'dictText' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建时间', field: 'createTime' },
  { title: '审核人', field: 'reviewer', formatter: 'dictText' },
  { title: '审核时间', field: 'reviewTime' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
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
    field: 'whCode',
    title: '仓库',
    itemRender: {
      name: 'WareHouseSelect',
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
    field: 'createBy@IN',
    title: '创建人',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
    },
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
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'bpmnStatus',
    label: '审核状态',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'orderStatus',
    label: '执行状态',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'closeStatus',
    label: '关闭状态',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'whCode',
    label: '仓库',
    component: 'WareHouseSelect',
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
];

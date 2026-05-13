import type { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'radio',
    width: 40,
  },
  { type: 'seq', width: 60 },
  { title: '工单编码', field: 'bizNo', width: 150 },
  { title: '紧急级别', field: 'bpmnSortFlag', slots: { default: 'bpmnSortFlag' }, width: 100 },
  { title: '源单编码', field: 'originalNo', width: 120 },
  { title: '维修区域', field: 'targetId', width: 120, formatter: 'dictText' },
  { title: '委外原因', field: 'reason', width: 250 },
  { title: '委外商', field: 'outsiders', width: 120, formatter: 'dictText' },
  { title: '最终价格', field: 'finalPrice', width: 120 },
  { title: '委外负责人', field: 'outManager', width: 100, formatter: 'dictText' },
  { title: '工单状态', field: 'orderStatus', formatter: 'dictText', width: 100 },
  // { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText', width: 120 },
  { title: '创建人', field: 'createBy', formatter: 'dictText', width: 100 },
  { title: '创建日期', field: 'createTime', width: 120 },
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
    title: '工单编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originalNo',
    title: '源单编码',
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

export const inquiryColumns: VxeGridPropTypes.Columns = [
  { type: 'seq', width: 60 },
  { title: '价格', field: 'unitPrice' },
  { title: '委外商编码', field: 'supplier' },
  { title: '委外商名称', field: 'supplierName' },
  { title: '备注', field: 'memo' },
  { title: '是否采用', field: 'isUse', formatter: 'dictText' },
  { title: '操作', field: 'operation', width: 150, fixed: 'right', slots: { default: 'action' } },
];

export const remarkColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  { title: '备注内容', field: 'remark' },
  { title: '填写人', field: 'fillPeople', formatter: 'dictText' },
  { title: '填写日期', field: 'fillDate' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const remarkFormSchema: FormSchema[] = [
  {
    field: 'remark',
    label: '备注内容',
    component: 'InputTextArea',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillPeople',
    label: '填报人',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillDate',
    label: '填报时间',
    component: 'DatePicker',
    colProps: {
      span: 24,
    },
    componentProps: {
      class: '!w-full',
    },
  },
];

export const InquiryFormSchema: FormSchema[] = [
  {
    field: 'unitPrice',
    label: '价格(元)',
    component: 'InputNumber',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'supplier',
    label: '委外商编码',
    component: 'SupplySelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (ids, rows) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            supplierName: rows?.pnName || '',
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'supplierName',
    label: '委外商名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 24,
    },
  },
];

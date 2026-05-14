import { VxeFormItemProps, VxeGridPropTypes, FormSchema, Rule } from '@mxpio/components';
import { positiveNumberPattern } from '@mxpio/bizcommon';

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
  { title: '加工商编码', field: 'pnCode' },
  { title: '加工商名称', field: 'pnName' },
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '单位', field: 'unitCode', formatter: 'dictText' },
  { title: '图号', field: 'drawingNo' },
  { title: '数量', field: 'quantity' },
  { title: '开始日期', field: 'startDate' },
  { title: '完成日期', field: 'deliverDate' },
  { title: '备注', field: 'memo' },
  { title: '创建时间', field: 'createTime' },
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
    field: 'itemCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'deliverDate',
    title: '完成日期',
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

const minRule: Rule = {
  pattern: positiveNumberPattern,
  trigger: 'blur',
  message: '请输入大于0的数字',
};

export const formSchema: FormSchema[] = [
  {
    field: 'itemCode',
    label: '物料编码',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemName',
    label: '物料名称',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemSpec',
    label: '规格型号',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'unitCode',
    label: '单位',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_UNIT_CODE',
        disabled: true,
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'quantity',
    label: '数量',
    component: 'Input',
    colProps: {
      span: 12,
    },
    required: true,
    componentProps: {
      type: 'number',
    },
    rules: [minRule],
  },
  {
    field: 'startDate',
    label: '开始日期',
    component: 'DatePicker',
    componentProps: () => {
      return {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        disabled: true,
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'deliverDate',
    label: '完工日期',
    component: 'DatePicker',
    componentProps: () => {
      return {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
];

export const toOutsourceOrderFormSchema: FormSchema[] = [
  {
    field: 'pnCode',
    label: '加工商',
    component: 'Select',
    colProps: {
      span: 12,
    },
    required: true,
  },
  {
    field: 'pnAddress',
    label: '加工商地址',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'pnContacts',
    label: '联系人',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'pnTel',
    label: '电话',
    component: 'Input',
    componentProps: () => {
      return {
        disabled: true,
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'bizMan',
    label: '业务员',
    component: 'UserByDeptSelect',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
];

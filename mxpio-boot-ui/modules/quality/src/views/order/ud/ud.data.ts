import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  { title: '不良品处理单号', field: 'bizNo' },
  { title: '质检单号', field: 'inspectionBillNo' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '申请事由', field: 'applyReason', formatter: 'dictText' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建时间', field: 'createTime' },
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
    title: '不良品处理单号',
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
    field: 'createBy@IN',
    title: '申请人',
    itemRender: {
      name: 'UserByDeptSelect',
    },
    span: 6,
  },
  {
    field: 'createTime@IN',
    title: '申请日期',
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
    label: '不良品处理单号',
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
    field: 'applyReason',
    label: '申请事由',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'atchFileNo',
    label: '申请附件',
    component: 'Upload',
  },
];

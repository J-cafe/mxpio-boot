import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
// import { dateUtil } from '@mxpio/utils';
// import XEUtils from 'xe-utils';

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
  { title: '请购单号', field: 'bizNo' },
  { title: '请购类型', field: 'bizType', formatter: 'dictText' },
  { title: '审批状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '单据日期', field: 'orderDate' },
  { title: '申请人', field: 'applyMan', formatter: 'dictText' },
  { title: '申请部门', field: 'applyDept', formatter: 'dictText' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
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
    field: 'bizNo',
    title: '请购单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bizType@EQ',
    title: '请购类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_ORDER_TYPE',
      },
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
    field: 'applyMan@EQ',
    title: '申请人',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    span: 6,
  },
  {
    field: 'applyDept@EQ',
    title: '申请部门',
    folding: true,
    itemRender: {
      name: 'DeptSelect',
      props: {
        multiple: false,
      },
    },
    span: 6,
  },
  {
    field: 'createTime',
    title: '创建日期',
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        class: '!w-full',
        autoFormat: true,
        // showTime: {
        //   defaultValue: [
        //     dateUtil('00:00:00', 'HH:mm:ss'), // 开始时间：00:00:00
        //     dateUtil('23:59:59', 'HH:mm:ss'), // 结束时间：23:59:59
        //   ],
        // },
      },
      // events: {
      //   change: (params: any, values) => {
      //     if (values && values.length === 2) {
      //       const [start, end] = values;
      //       // 开始时间附加 00:00:00
      //       const formattedStart = start ? dateUtil(start).format('YYYY-MM-DD 00:00:00') : null;
      //       // 结束时间附加 23:59:59
      //       const formattedEnd = end ? dateUtil(end).format('YYYY-MM-DD 23:59:59') : null;
      //       const { data } = params;
      //       XEUtils.set(data, 'createTime', [formattedStart, formattedEnd]);
      //     }
      //   },
      // },
    },
    folding: true,
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
    label: '请购单号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'bizType',
    label: '请购类型',
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
    field: 'applyMan',
    label: '申请人',
    required: true,
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'applyDept',
    label: '申请部门',
    required: true,
    component: 'DeptSelect',
    componentProps: {
      multiple: false,
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

export const checkFormSchema: FormSchema[] = [
  {
    field: 'auditOpinion',
    label: '审核意见',
    required: true,
    component: 'Select',
    componentProps: () => {
      return {
        options: [
          { label: '同意', value: '01' },
          { label: '不同意', value: '02' },
          { label: '驳回', value: '03' },
        ],
      };
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'auditSuggest',
    label: '审核原因',
    component: 'Input',
    required: true,
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

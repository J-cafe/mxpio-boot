import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '签入单编号',
    field: 'code',
  },
  {
    title: '审核状态',
    field: 'bpmnStatus',
    formatter: 'dictText',
  },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 120,
    sortable: true,
  },
  {
    title: '物料名称',
    field: 'item.itemName',
    width: 100,
    sortable: true,
  },
  {
    title: '工序编码',
    field: 'proc.processCode',
  },
  {
    title: '工序名称',
    field: 'proc.processName',
  },
  {
    title: '签入说明',
    field: 'signInDesc',
  },
  {
    title: '审核状态',
    field: 'bpmnStatus',
    formatter: 'dictText',
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
  },
  {
    title: '创建日期',
    field: 'createTime',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
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
    field: 'bpmnStatus@EQ',
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
    field: 'code',
    label: '签入单编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemCode',
    label: '物料编码',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'itemName',
    label: '物料名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'itemSpec',
    label: '规格型号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
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
    field: 'fileNo',
    label: '图纸',
    component: 'Upload',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'createBy',
    label: '创建人',
    component: 'UserByDeptSelect',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'createTime',
    label: '创建日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      class: 'w-full',
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'signInDesc',
    label: '签入说明',
    component: 'InputTextArea',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
];

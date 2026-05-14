import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '检测方案编号', field: 'code' },
  { title: '检测方案名称', field: 'name' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '适用业务类型', field: 'busiType', formatter: 'dictText' },
  { title: '方案说明', field: 'description' },
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
    field: 'code',
    title: '编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'name',
    title: '名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'busiType@EQ',
    title: '适用业务类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_IS_BUSI_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'createBy@EQ',
    title: '创建人',
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
    field: 'createTime',
    title: '创建时间',
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
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
    field: 'code',
    label: '方案编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: (data) => {
      console.log(data);
      return {
        disabled: true,
      };
    },
  },
  {
    field: 'name',
    label: '方案名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 8,
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
      span: 8,
    },
  },
  {
    field: 'busiType',
    label: '适用业务类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_IS_BUSI_TYPE',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'description',
    label: '方案说明',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'attachment',
    label: '附件',
    component: 'Upload',
    colProps: {
      span: 8,
    },
  },
];

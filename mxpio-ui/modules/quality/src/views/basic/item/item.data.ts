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
  { title: '检测项目编号', field: 'code' },
  { title: '项目名称', field: 'name' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '项目分类', field: 'itemClass', formatter: 'dictText' },
  { title: '项目类型', field: 'itemType', formatter: 'dictText' },
  { title: '缺陷等级', field: 'defectGrade', formatter: 'dictText' },
  { title: '检测标准', field: 'standard' },
  { title: '检测工具', field: 'detectionTool' },
  { title: '检测方法', field: 'detectionMethod' },
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
    title: '项目编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'name',
    title: '项目名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemClass@EQ',
    title: '项目分类',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_II_CLASS',
      },
    },
    span: 6,
  },
  {
    field: 'itemType@EQ',
    title: '项目类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_II_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'defectGrade@EQ',
    title: '缺陷等级',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_II_DEFECT_GRADE',
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
    label: '项目编号',
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
    label: '项目名称',
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
    field: 'itemClass',
    label: '项目分类',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_II_CLASS',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'itemType',
    label: '项目类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_II_TYPE',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'defectGrade',
    label: '缺陷等级',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_II_DEFECT_GRADE',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'standard',
    label: '检验标准',
    component: 'Input',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'detectionTool',
    label: '检测工具',
    component: 'Input',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'detectionMethod',
    label: '检验方法',
    component: 'Input',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'technicalRequirements',
    label: '技术要求',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
];

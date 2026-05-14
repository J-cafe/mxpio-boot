import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  { title: '审核状态', field: 'useType', formatter: 'dictText' },
  { title: '物料编码', field: 'productItemCode' },
  { title: '物料名称', field: 'productItemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '图号', field: 'productDrawingNo' },
  { title: '生效日期', field: 'beginTime' },
  { title: '变更人', field: 'changeMan', formatter: 'dictText' },
  { title: '变更日期', field: 'changeDate' },
  { title: '变更说明', field: 'changeMemo' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'productItemCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'productItemName',
    title: '物料名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemSpec',
    title: '规格型号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'parentGroup@EQ',
    title: '审核状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_USE_TYPE',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'changeMan@EQ',
    title: '变更人',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'changeDate',
    title: '变更日期',
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
    field: 'productItemCode',
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
    field: 'productItemName',
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
    field: 'productDrawingNo',
    label: '图号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'beginTime',
    label: '生效日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      class: 'w-full',
      disabled: true,
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeMan',
    label: '变更人',
    component: 'UserByDeptSelect',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeDate',
    label: '变更日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      class: 'w-full',
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeMemo',
    label: '变更说明',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeAttachFile',
    label: '变更附件',
    component: 'Upload',
    colProps: {
      span: 8,
    },
  },
];

export const formAuditSchema: FormSchema[] = [
  {
    field: 'agree',
    label: '是否同意',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'MB_SYSTEM_YES_NO',
      };
    },
    colProps: {
      span: 8,
    },
    required: true,
  },
  {
    field: 'auditOpinion',
    label: '审核意见',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
];

export const detailColumns: VxeGridPropTypes.Columns = [
  { title: '序号', field: 'processOrder', sortable: true, width: 80 },
  { title: '工序段编码', field: 'procGroupCode', width: 120 },
  { title: '工序段名称', field: 'procGroupName', width: 120 },
  { title: '工序编码', field: 'processCode', width: 100 },
  { title: '工序名称', field: 'processInfo.processName', width: 100 },
  { title: '工序类型', field: 'processInfo.textMap.processType$DICT_TEXT_', width: 100 },
  { title: '报工方式', field: 'processInfo.textMap.reportMode$DICT_TEXT_', width: 100 },
  { title: '是否分批报工', field: 'processInfo.textMap.batchReportAble$DICT_TEXT_', width: 120 },
  { title: '是否自制', field: 'processInfo.textMap.manufactureAble$DICT_TEXT_', width: 100 },
  { title: '是否委外', field: 'processInfo.textMap.outsourceAble$DICT_TEXT_', width: 100 },
  { title: '误差范围（%）', field: 'processInfo.toleranceRange', width: 120 },
  { title: '是否自动报工', field: 'processInfo.textMap.autoReportAble$DICT_TEXT_', width: 120 },
  { title: '标准准备工时（S）', field: 'preparationTime', width: 180 },
  { title: '单件标准工时（S）', field: 'singlePieceTime', width: 180 },
  { title: '质检方案编码', field: 'schemeCode', width: 150 },
  { title: '质检方案名称', field: 'schemeName', slots: { default: 'schemeName' }, width: 150 },
];

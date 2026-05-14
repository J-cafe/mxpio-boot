import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'radio', width: 40 },
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '物料编码', field: 'productItemCode' },
  { title: '物料名称', field: 'productItemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '图号', field: 'productDrawingNo' },
  { title: '生效日期', field: 'beginTime' },
  {
    title: '操作',
    field: 'operation',
    fixed: 'right',
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
    span: 8,
  },
  {
    field: 'productItemName',
    title: '物料名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'productDrawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 8,
  },
  {
    span: 8,
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

export const procBomDetailColumns: VxeGridPropTypes.Columns = [
  { type: 'radio', width: 40 },
  { title: '序号', field: 'processOrder', width: 80, sortable: true },
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
  { title: '标准准备工时（S）', field: 'preparationTime', width: 160 },
  { title: '单件标准工时（S）', field: 'singlePieceTime', width: 160 },
  { title: '质检方案编码', field: 'schemeCode', width: 150 },
  { title: '质检方案名称', field: 'schemeName', width: 150 },
];

export const procDrawingfileColumns: VxeGridPropTypes.Columns = [
  { title: '文件名称', field: 'fileName', slots: { default: 'fileName' }, width: 200 },
  { title: '文件大小', field: 'length', width: 80 },
  { title: '版本', field: 'version', width: 80 },
  { title: '版次', field: 'edition', width: 80, slots: { default: 'edition' } },
  { title: '是否签出', field: 'signOffFlag', width: 80, formatter: 'dictText' },
  { title: '签出人', field: 'signOffer', width: 100, formatter: 'dictText' },
  { title: '文件描述', field: 'remarks', width: 120 },
  { title: '签入说明', field: 'signInDesc', width: 120 },
  { title: '创建人', field: 'createBy', width: 120, formatter: 'dictText' },
  { title: '创建时间', field: 'createTime', width: 120 },
  {
    title: '操作',
    field: 'operation',
    fixed: 'right',
    slots: { default: 'action' },
    width: 120,
  },
];

export const editionColumns: VxeGridPropTypes.Columns = [
  { title: '文件名称', field: 'fileName', slots: { default: 'fileName' }, width: 200 },
  { title: '文件大小', field: 'length' },
  { title: '版次', field: 'edition' },
  { title: '签出人', field: 'signOffer', formatter: 'dictText' },
  { title: '签入时间', field: 'signInTime' },
  { title: '签入说明', field: 'signInDesc' },
];

export const historyColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  { title: '物料编码', field: 'productItemCode' },
  { title: '物料名称', field: 'productItemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '图号', field: 'productDrawingNo' },
  { title: '生效日期', field: 'beginTime' },
  { title: '版本号', field: 'version' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

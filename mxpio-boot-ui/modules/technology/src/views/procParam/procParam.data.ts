import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '物料编码', field: 'productItemCode' },
  { title: '物料名称', field: 'productItemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '图号', field: 'productDrawingNo' },
  { title: '生效日期', field: 'beginTime' },
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
  { title: '质检方案名称', field: 'schemeName', slots: { default: 'schemeName' }, width: 150 },
];

export const procEqpColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  { title: '设备编码', field: 'eqpCode' },
  { title: '设备名称', field: 'eqpName' },
  { title: '设备类型', field: 'eqpClass' },
  { title: '数量', field: 'eqpNum', width: 80 },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

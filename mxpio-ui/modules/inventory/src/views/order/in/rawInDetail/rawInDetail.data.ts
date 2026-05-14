import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

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
  { title: '仓单编号', field: 'bizNo', width: 120 },
  { title: '仓库', field: 'whCode', width: 100, formatter: 'dictText' },
  { title: '仓单类型', field: 'warehouseOrder.textMap.woType$DICT_TEXT_', width: 100 },
  { title: '行号', field: 'lineNo', width: 80, sortable: true },
  { title: '行状态', field: 'closeStatus', width: 80, formatter: 'dictText' },
  { title: '物料编码', field: 'itemCode', width: 80 },
  { title: '物料名称', field: 'itemName', width: 80 },
  { title: '规格型号', field: 'itemSpec', width: 80 },
  { title: '图号', field: 'drawingNo', width: 80 },
  { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
  { title: '数量', field: 'quantity', width: 80 },
  { title: '批次号', field: 'lotNo', width: 120 },
  { title: '已执行数量', field: 'actualQuantity', width: 120 },
  { title: '源单单号', field: 'originBizNo', width: 120 },
  { title: '源单行号', field: 'originBizLineNo', width: 100 },
  { title: '通知单编号', field: 'originNoticeNo', width: 100 },
  { title: '制单人', field: 'warehouseOrder.textMap.createBy$DICT_TEXT_', width: 100 },
  { title: '制单时间', field: 'warehouseOrder.createTime', width: 100 },
  { title: '制单人部门', field: 'warehouseOrder.textMap.createDept$DICT_TEXT_', width: 100 },
  { title: '备注', field: 'memo', width: 80 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'originBizNo',
    title: '源单编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originNoticeNo',
    title: '通知单编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bizNo',
    title: '单据编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'whCode',
    title: '仓库',
    folding: true,
    itemRender: {
      name: 'WareHouseSelect',
    },
    span: 6,
  },
  {
    field: 'itemCode',
    title: '物料编码',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemName',
    title: '物料名称',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'drawingNo',
    title: '图号',
    folding: true,
    itemRender: {
      name: 'AInput',
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

export const executeColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '仓库', field: 'whCode', width: 120, formatter: 'dictText' },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 100,
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 120,
  },
  {
    title: '规格型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 80,
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '数量',
    field: 'quantity',
    width: 80,
  },
  { title: '允差数量', field: 'toleranceRangeQuantity', width: 80 },
  { title: '已执行数量', field: 'actualQuantity', width: 100 },
  {
    title: '最大执行数量',
    field: 'maxQuantity',
    width: 100,
  },
  {
    title: '执行数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
    },
    width: 120,
  },
  {
    title: '批次号',
    field: 'lotNo',
    width: 120,
  },
];

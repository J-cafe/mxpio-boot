import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '销售单号', field: 'bizNo', width: 120, fixed: 'left' },
  { title: '行号', field: 'lineNo', width: 60, fixed: 'left' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '订单数量', field: 'quantity', width: 100 },
  { title: '已冲销数量', field: 'actualQuantity', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 100 },
  { title: '交付日期', field: 'deliverDate', width: 100 },
  { title: '创建时间', field: 'createTime', width: 120 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '销售单号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'itemCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'itemName',
    title: '物料名称',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 8,
  },
  {
    field: 'drawingNo',
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

export const forecastDetailColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '预测单号', field: 'spBizNo' },
  { title: '行号', field: 'soBizLineNo', width: 60 },
  { title: '冲销数量', field: 'quantity' },
  { title: '预测数量', field: 'spQuantity' },
  { title: '预测日期', field: 'demandDate' },
];

export const forecastDetailOutColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '预测单号', field: 'bizNo', width: 120 },
  { title: '行号', field: 'lineNo', width: 60 },
  { title: '行状态', field: 'closeStatus', width: 100, formatter: 'dictText' },
  {
    title: '冲销数量',
    field: 'executeQuantity',
    editRender: { name: 'AInputNumber' },
    width: 120,
  },
  { title: '未冲销数量', field: 'restQuantity', width: 120 },
  { title: '预测数量', field: 'quantity', width: 100 },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', width: 100, formatter: 'dictText' },
  { title: '创建时间', field: 'createTime', width: 120 },
];

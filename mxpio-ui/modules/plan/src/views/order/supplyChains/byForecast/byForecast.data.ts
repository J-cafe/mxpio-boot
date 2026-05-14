import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '预测单号',
    field: 'bizNo',
  },
  {
    title: '物料编码',
    field: 'itemCode',
  },
  {
    title: '物料名称',
    field: 'itemName',
  },
  {
    title: '规格型号',
    field: 'itemSpec',
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
  },
  {
    title: '图号',
    field: 'drawingNo',
  },
  {
    title: '物料来源',
    field: 'itemSource',
    formatter: 'dictText',
  },
  {
    title: '可委外',
    field: 'outsourceAble',
    formatter: 'dictText',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
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

export const saleDetailColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '客户编码', field: 'pnCode', width: 120 },
  { title: '客户名称', field: 'pnName', width: 120 },
  { title: '销售单号', field: 'soBizNo', width: 120 },
  { title: '行号', field: 'soBizLineNo', width: 60 },
  { title: '冲销数量', field: 'quantity', width: 120 },
  { title: '单据数量', field: 'soQuantity', width: 120 },
  { title: '交付日期', field: 'deliverDate', width: 100 },
];

export const saleDetailOutColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '客户编码', field: 'pnCode' },
  { title: '客户名称', field: 'pnName' },
  { title: '销售单号', field: 'bizNo' },
  { title: '行号', field: 'lineNo', width: 60 },
  {
    title: '冲销数量',
    field: 'executeQuantity',
    editRender: { name: 'AInputNumber' },
  },
  { title: '已冲销数量', field: 'actualQuantity' },
  { title: '单据数量', field: 'quantity' },
  { title: '交付日期', field: 'deliverDate' },
  { title: '创建时间', field: 'createTime' },
];

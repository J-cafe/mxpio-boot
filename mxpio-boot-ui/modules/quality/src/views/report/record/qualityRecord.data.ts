import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center', fixed: 'left' },
  { title: '订单号', field: 'orderBizNo', width: 120 },
  { title: '订单类型', field: 'originBizType', width: 100, formatter: 'dictText' },
  { title: '订单状态', field: 'orderStatus', width: 100, formatter: 'dictText' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '订单数量', field: 'orderLineQuantity', width: 100 },
  { title: '验收单号', field: 'qoNoticeNo', width: 100 },
  { title: '交检单位', field: 'applyOrgName', width: 100 },
  { title: '交检日期', field: 'createTime', width: 100 },
  { title: '交检数量', field: 'quantity', width: 100 },
  { title: '质检单号', field: 'ibBizNo', width: 100 },
  { title: '检验日期', field: 'inspectionStartTime', width: 100 },
  { title: '抽检数量', field: 'checkQuantity', width: 100 },
  { title: '抽检比例', field: 'checkRate', width: 100 },
  { title: '不合格数量', field: 'unqualifiedQuantity', width: 120 },
  { title: '交检合格率', field: 'passRate', width: 120 },
  { title: '质检员', field: 'inspector', width: 100, formatter: 'dictText' },
  { title: '入库时间', field: 'lastStockTime', width: 100 },
  { title: '入库数量', field: 'stockQuantity', width: 100 },
  { title: '入库人', field: 'stockMan', width: 100, formatter: 'dictText' },
  { title: '不合格类型', field: 'unqualifiedType', formatter: 'dictText', width: 120 },
  { title: '不合格描述', field: 'unqualifiedDesc', width: 120 },
  { title: '备注', field: 'memo', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'orderBizNo',
    title: '订单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originBizType@IN',
    title: '订单类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_ORIGIN_BIZ_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'itemCode',
    title: '物料编码',
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
    field: 'qoNoticeNo',
    title: '验收单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'ibBizNo',
    title: '质检单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'createTime@EQ',
    title: '交检日期',
    folding: true,
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
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

import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '计划编号', field: 'code' },
  { title: '计划名称', field: 'name' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '生效状态', field: 'effectiveStatus', formatter: 'dictText' },
  { title: '类别', field: 'type', formatter: 'dictText' },
  { title: '开始时间', field: 'startDate' },
  { title: '结束时间', field: 'endDate' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 100,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'code',
    title: '计划编号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'name',
    title: '计划名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'bpmnStatus@EQ',
    title: '审核状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
      },
    },
    folding: true,
    span: 8,
  },
  {
    field: 'orderType@EQ',
    title: '生效状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_SALES_ORDER_TYPE',
      },
    },
    folding: true,
    span: 8,
  },
  {
    field: 'type@EQ',
    title: '计划类别',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_PLAN_TYPE',
      },
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

export const saleLinesColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '销售单号', field: 'bizNo' },
  { title: '销售单行号', field: 'lineNo' },
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '单位', field: 'unitCode', formatter: 'dictText' },
  { title: '图号', field: 'drawingNo' },
  { title: '交付日期', field: 'deliverDate' },
  { title: '数量', field: 'quantity' },
];

export const saleSearchFormSchema: VxeFormItemProps[] = [
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
    span: 8,
    folding: true,
  },
  {
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
    folding: true,
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

export const saleForecastLinesColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '预测单号', field: 'bizNo' },
  { title: '预测单行号', field: 'lineNo' },
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '单位', field: 'unitCode', formatter: 'dictText' },
  { title: '图号', field: 'drawingNo' },
  { title: '需求日期', field: 'demandDate' },
  { title: '数量', field: 'quantity' },
];

export const saleForecastSearchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '预测单号',
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
    span: 8,
    folding: true,
  },
  {
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
    folding: true,
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

export const odLinesColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { title: '需求单号', field: 'bizNo' },
  { title: '需求单行号', field: 'lineNo' },
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '单位', field: 'unitCode', formatter: 'dictText' },
  { title: '图号', field: 'drawingNo' },
  { title: '交付日期', field: 'demandDate' },
  { title: '数量', field: 'quantity' },
];

export const odSearchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '预测单号',
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
    span: 8,
    folding: true,
  },
  {
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
    folding: true,
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

export const mainPlanLineColumns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: 50, align: 'center' },
  { title: '需求类型', field: 'dataType', width: 100, formatter: 'dictText' },
  { title: '业务单号', field: 'bizNo', width: 100 },
  { title: '业务单行号', field: 'bizLineNo', width: 100 },
  { title: '物料编码', field: 'itemCode', width: 120 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '需求数量', field: 'needQuantity', width: 100 },
  { title: '需求日期', field: 'needDate', width: 100 },
  {
    title: '计划交付日期',
    field: 'deliveryDate',
    width: 120,
  },
  {
    title: '备注',
    field: 'memo',
    width: 120,
  },
];

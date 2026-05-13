import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center', fixed: 'left' },
  { title: '单据日期', field: 'orderDate', width: 100, fixed: 'left', sortable: true },
  { title: '采购单号', field: 'bizNo', width: 120, fixed: 'left', sortable: true },
  { title: '物料编码', field: 'itemCode', width: 120, fixed: 'left', sortable: true },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '物料组', field: 'itemGroupCode', width: 100, formatter: 'dictText' },
  { title: '图号', field: 'drawingNo', width: 100, slots: { default: 'drawingNo' } },
  { title: '执行状态', field: 'orderStatus', width: 100, formatter: 'dictText' },
  { title: '供应商编码', field: 'pnCode', width: 100, formatter: 'dictText' },
  { title: '供应商名称', field: 'pnName', width: 100 },
  { title: '业务员', field: 'bizMan', width: 100, formatter: 'dictText' },
  { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
  { title: '采购数量', field: 'orderQuantity', width: 100 },
  { title: '交检数量', field: 'receiveQuantity', width: 100 },
  { title: '实际交货数量', field: 'stockQuantity', width: 120 },
  { title: '到货日期', field: 'suggestArriveDate', width: 100 },
  { title: '验收日期', field: 'lastInspectionFinishTime', width: 100 },
  { title: '实际到货日期', field: 'lastStockTime', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'orderDate@EQ',
    title: '单据日期',
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'yyyy-MM-dd',
      },
      attrs: {
        class: '!w-full',
      },
    },
    span: 6,
  },
  {
    field: 'bizNo',
    title: '采购单号',
    itemRender: {
      name: 'AInput',
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
    field: 'orderStatus@IN',
    title: '执行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'pnCode',
    title: '供应商',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bizMan@IN',
    title: '业务员',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: true,
      },
    },
    span: 6,
  },
  {
    field: 'suggestArriveDate@EQ',
    title: '到货日期',
    folding: true,
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'yyyy-MM-dd',
      },
      attrs: {
        class: '!w-full',
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

import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '仓库',
    field: 'whCode',
    width: 100,
    formatter: 'dictText',
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
    title: '库存数量',
    field: 'allQuantity',
  },
  {
    title: '可用库存数量',
    field: 'quantity',
  },
  {
    title: '占用库存数量',
    field: 'holdQuantity',
  },
  {
    title: '规格型号',
    field: 'itemSpec',
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'parentCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'parentName',
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
    span: 8,
    folding: true,
  },
  {
    field: 'parentGroup@EQ',
    title: '物料组',
    itemRender: {
      name: 'ItemGroupSelect',
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

export const soLineColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
    fixed: 'left',
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
    fixed: 'left',
  },
  {
    title: '客户名称',
    field: 'salesOrder.pnName',
    treeNode: true,
    width: 120,
    fixed: 'left',
  },
  {
    title: '销售单号',
    field: 'bizNo',
    width: 100,
  },
  {
    title: '行号',
    field: 'lineNo',
    width: 60,
  },
  {
    title: '发货数量',
    field: 'executeQuantity',
    editRender: { name: 'AInput' },
    width: 120,
  },
  {
    title: '最大可发货数',
    field: 'maxQuantity',
    width: 100,
    slots: {
      default: ({ row }) => {
        row.maxQuantity =
          Number(row.quantity || 0) -
          Number(row.actualQuantity || 0) -
          Number(row.planQuantity || 0) +
          Number(row.actualRejectQuantity || 0);
        return row.maxQuantity;
      },
    },
  },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 100,
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 100,
  },
  {
    title: '规格型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 100,
  },
  {
    title: '单位',
    field: 'unitCode',
    width: 100,
    formatter: 'dictText',
  },
  {
    title: '需求数量',
    field: 'quantity',
    width: 100,
  },
  {
    title: '交付日期',
    field: 'deliverDate',
    width: 100,
  },
  {
    title: '实际发货数',
    field: 'actualQuantity',
    width: 100,
  },
  {
    title: '计划发货数',
    field: 'planQuantity',
    width: 100,
  },
  {
    title: '实际退货数',
    field: 'actualRejectQuantity',
    width: 100,
  },
];

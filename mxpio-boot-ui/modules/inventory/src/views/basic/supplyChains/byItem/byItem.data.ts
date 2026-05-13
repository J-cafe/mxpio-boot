import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
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

export const supplyColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '供应商编码',
    field: 'supply.pnCode',
  },
  {
    title: '供应商名称',
    field: 'supply.pnName',
  },
  {
    title: '单价',
    field: 'itemPrice',
  },
  {
    title: '是否默认',
    field: 'defaultSupply',
    formatter: 'dictText',
  },
  {
    title: '是否检验',
    field: 'inQualityControl',
    formatter: 'dictText',
  },
  {
    title: '是否冻结',
    field: 'hold',
    formatter: 'dictText',
  },
];

export const supplyOutColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '供应商编码',
    field: 'pnCode',
  },
  {
    title: '供应商名称',
    field: 'pnName',
  },
  {
    title: '简称',
    field: 'pnAbbr',
  },
  {
    title: '单价',
    field: 'itemPrice',
    editRender: { name: 'AInputNumber' },
  },
  {
    title: '业务员',
    field: 'bizMan',
    formatter: 'dictText',
  },
];

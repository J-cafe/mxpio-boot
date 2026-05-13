import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '仓库',
    field: 'whCode',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '批次号',
    field: 'lotNo',
    width: 120,
  },
  {
    title: '批次索引号',
    field: 'subLotIndex',
    width: 100,
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
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 80,
  },
  {
    title: '出库数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
    },
    width: 120,
  },
  {
    title: '库存数量',
    field: 'quantity',
    width: 80,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'lotNo',
    title: '批次号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    span: 8,
    className: '!pr-0',
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

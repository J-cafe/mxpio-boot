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

    sortable: true,
  },
  {
    title: '物料编码',
    field: 'itemCode',
  },
  {
    title: '图号',
    field: 'drawingNo',
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
    title: '物料组',
    field: 'itemGroupCode',
    formatter: 'dictText',
  },
  {
    title: '批次号',
    field: 'lotNo',
  },
  {
    title: '数量',
    field: 'quantity',
  },
  {
    title: '批次索引号',
    field: 'subLotIndex',
  },
  {
    title: '批次状态',
    field: 'lotStatus',
    slots: {
      default: ({ row }) => {
        if (row.lotStatus === '0000000000000000' || !row.lotStatus) {
          return '';
        }
        return '已锁定';
      },
    },
  },
  {
    title: '创建日期',
    field: 'createTime',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'whCode@EQ',
    title: '仓库',
    itemRender: {
      name: 'WareHouseSelect',
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
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'lotNo',
    title: '批次号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemGroupCode@EQ',
    title: '物料组',
    folding: true,
    itemRender: {
      name: 'ItemGroupSelect',
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

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
    title: '出入类型',
    field: 'accessType',
    slots: {
      default: ({ row }) => {
        if (row.accessType === 'in') {
          return '入单';
        }
        if (row.accessType === 'out') {
          return '出单';
        }
        return '';
      },
    },
  },
  {
    title: '仓单类型',
    field: 'woBizType',
    formatter: 'dictText',
  },
  {
    title: '仓单单号',
    field: 'woBizNo',
    formatter: 'dictText',
  },
  {
    title: '仓单行号',
    field: 'woBizLineNo',
  },
  {
    title: '来源单号',
    field: 'originBizNo',
    formatter: 'dictText',
  },
  {
    title: '来源单行号',
    field: 'originBizLineNo',
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
    field: 'woBizNo',
    title: '仓单单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originBizNo',
    title: '来源单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'woBizType@EQ',
    title: '仓单类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_WO_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'accessType@EQ',
    title: '出入类型',
    folding: true,
    itemRender: {
      name: 'ASelect',
      props: {
        options: [
          { label: '入单', value: 'in' },
          { label: '出单', value: 'out' },
        ],
        class: '!w-full',
      },
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
    field: 'createTime',
    title: '创建日期',
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD HH:mm:ss',
        class: '!w-full',
        autoFormat: true,
      },
    },
    folding: true,
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

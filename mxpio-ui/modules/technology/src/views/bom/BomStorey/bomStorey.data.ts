import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { dateUtil } from '@mxpio/utils';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '物料编码',
    field: 'parentCode',
    width: 120,
    sortable: true,
  },
  {
    title: '物料名称',
    field: 'parentName',
  },
  {
    title: '图号',
    field: 'parentDrawingNo',
  },
  {
    title: '版本号',
    field: 'version',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
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

export const subColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
    fixed: 'left',
  },
  {
    title: '物料编码',
    field: 'itemCode',
    treeNode: true,
    width: 150,
    fixed: 'left',
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 100,
    fixed: 'left',
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
    title: '物料来源',
    field: 'itemSource',
    width: 100,
    formatter: 'dictText',
  },
  {
    title: '物料组',
    field: 'itemProp',
    width: 100,
    formatter: 'dictText',
  },
  {
    title: '是否原材料',
    field: 'material',
    width: 100,
    formatter: 'dictText',
  },
  {
    title: '材料牌号',
    field: 'materialBrand',
    width: 100,
  },
  {
    title: '材料类型',
    field: 'materialType',
    width: 100,
    formatter: 'dictText',
  },
  {
    title: '生效日期',
    field: 'beginTime',
    width: 100,
    slots: {
      default: ({ row }) => {
        return dateUtil(row.beginTime).format('YYYY-MM-DD');
      },
    },
  },
  {
    title: '失效日期',
    field: 'endTime',
    width: 100,
    slots: {
      default: ({ row }) => {
        return dateUtil(row.beginTime).format('YYYY-MM-DD');
      },
    },
  },
  {
    title: '基本用量',
    field: 'itemConsumption',
    width: 100,
  },
  {
    title: '下料长度',
    field: 'makeLength',
    width: 100,
  },
  {
    title: '下料宽度',
    field: 'makeArea',
    width: 100,
  },
  {
    title: '可制数量',
    field: 'makeNum',
    width: 100,
  },
  {
    title: '损耗率',
    field: 'wastage',
    width: 100,
  },
];

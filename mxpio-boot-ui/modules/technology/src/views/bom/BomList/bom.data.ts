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
    title: '父物料编码',
    field: 'parentCode',
    width: 120,
    sortable: true,
  },
  {
    title: '父物料名称',
    field: 'parentName',
  },
  {
    title: '父规格型号',
    field: 'parentSpec',
  },
  {
    title: '父物料单位',
    field: 'parentUnit',
    formatter: 'dictText',
  },
  {
    title: '父物料图号',
    field: 'parentDrawingNo',
  },
  {
    title: '物料组',
    field: 'parentGroup',
    formatter: 'dictText',
  },
  {
    title: '材料牌号',
    field: 'materialBrand',
  },
  {
    title: '使用状态',
    field: 'useType',
    formatter: 'dictText',
  },
  {
    title: '生效日期',
    field: 'beginTime',
  },
  {
    title: '版本号',
    field: 'version',
  },
  {
    title: '变更单号',
    field: 'changeCode',
  },
  {
    title: '变更人',
    field: 'changeMan',
    formatter: 'dictText',
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
  },
  {
    title: '创建日期',
    field: 'createTime',
  },

  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const historyColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '父物料编码',
    field: 'parentCode',
    width: 120,
  },
  {
    title: '父物料名称',
    field: 'parentName',
  },
  {
    title: '父规格型号',
    field: 'parentSpec',
  },
  {
    title: '父物料单位',
    field: 'parentUnit',
    formatter: 'dictText',
  },
  {
    title: '父物料图号',
    field: 'parentDrawingNo',
  },
  {
    title: '物料组',
    field: 'parentGroup',
    formatter: 'dictText',
  },
  {
    title: '生效日期',
    field: 'beginTime',
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

export const endLessColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '物料编码',
    field: 'itemCode',
    sortable: true,
  },
  {
    title: 'BOM路径',
    field: 'path',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
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
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemSpec',
    title: '规格型号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemGroupCode@EQ',
    title: '物料组',
    itemRender: {
      name: 'ItemGroupSelect',
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

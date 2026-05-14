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
    field: 'parentCode',
    width: 120,
    sortable: true,
  },
  {
    title: '物料名称',
    field: 'parentName',
    width: 100,
  },
  {
    title: '规格型号',
    field: 'parentSpec',
    width: 100,
  },
  {
    title: '单位',
    field: 'parentUnit',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '物料组',
    field: 'parentGroup',
    formatter: 'dictText',
    width: 100,
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
    field: 'itemSpec',
    title: '规格型号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
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

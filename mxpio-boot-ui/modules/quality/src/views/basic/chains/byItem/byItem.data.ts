import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'seq', width: 60 },
  { type: 'expand', width: 40, slots: { content: 'expand_content' } },
  { title: '检测方案编号', field: 'code' },
  { title: '检测方案名称', field: 'name' },
  { title: '适用业务类型', field: 'busiType', formatter: 'dictText' },
  { title: '方案说明', field: 'description' },
];

export const searchFormItemSchema: VxeFormItemProps[] = [
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

export const itemColumns: VxeGridPropTypes.Columns = [
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '图号', field: 'drawingNo' },
  { title: '物料来源', field: 'itemSource', formatter: 'dictText' },
  { title: '可自制', field: 'manufactureAble', formatter: 'dictText' },
  { title: '可委外', field: 'outsourceAble', formatter: 'dictText' },
];

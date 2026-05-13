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
    title: '顶级物料编码',
    field: 'topCode',
    width: 120,
  },
  {
    title: '顶级物料名称',
    field: 'topName',
    width: 100,
  },
  {
    title: '顶级物料型号',
    field: 'topSpec',
    width: 100,
  },
  {
    title: '顶级物料单位',
    field: 'topUnit',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '顶级物料图号',
    field: 'topDrawingNo',
    width: 100,
  },
  {
    title: '父物料编码',
    field: 'parentCode',
    width: 100,
  },
  {
    title: '父物料名称',
    field: 'parentName',
    width: 100,
  },
  {
    title: '父物料型号',
    field: 'parentSpec',
    width: 100,
  },
  {
    title: '父物料单位',
    field: 'parentUnit',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '父物料图号',
    field: 'parentDrawingNo',
    width: 100,
  },
  {
    title: '子物料编码',
    field: 'itemCode',
    width: 100,
  },
  {
    title: '子物料名称',
    field: 'itemName',
    width: 100,
  },
  {
    title: '子物料型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '子物料单位',
    field: 'itemUnit',
    width: 100,
  },
  {
    title: '子物料图号',
    field: 'drawingNo',
    width: 100,
  },
  {
    title: '子物料组',
    field: 'itemProp',
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
    field: 'beginTime',
    title: '单据日期',
    itemRender: {
      name: 'ADatePicker',
      attrs: { class: '!w-full' },
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

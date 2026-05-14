import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '资产编码', field: 'basicsCode', width: 100 },
  { title: '设备序列号', field: 'seqNumber', width: 100 },
  { title: '资产名称', field: 'eqpName', width: 100 },
  { title: '设备编码', field: 'eqpCode', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '规格型号', field: 'specType', width: 100 },
  { title: '制造商', field: 'manufacturer', width: 100 },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '备注', field: 'remarks', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'basicsCode',
    title: '资产编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'eqpName',
    title: '资产名称',
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

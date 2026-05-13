import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '设备编码', field: 'eqpCode', width: 100 },
  { title: '设备名称', field: 'eqpName', width: 100 },
  { title: '规格型号', field: 'specType', width: 100 },
  { title: '设备分类', field: 'eqpTypeCode', formatter: 'dictText', width: 100 },
  { title: '是否关键设备', field: 'isKey', formatter: 'dictText', width: 100 },
  { title: '是否ESA设备', field: 'isEsa', formatter: 'dictText', width: 100 },
  { title: '是否数控设备', field: 'isDigital', formatter: 'dictText', width: 100 },
  { title: '是否附属设备', field: 'isSubOrdinate', formatter: 'dictText', width: 100 },
  { title: '是否检定', field: 'isCalibrate', formatter: 'dictText', width: 100 },
  { title: '检定周期', field: 'calibrateCycle', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'eqpName',
    title: '设备名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'eqpCode',
    title: '设备编码',
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

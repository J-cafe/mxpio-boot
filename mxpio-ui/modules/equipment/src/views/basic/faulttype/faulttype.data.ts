import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '故障类型编码', field: 'typeCode' },
  { title: '故障类型名称', field: 'typeName' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建日期', field: 'createTime' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'typeCode',
    title: '故障类型编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'typeName',
    title: '故障类型名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    span: 6,
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

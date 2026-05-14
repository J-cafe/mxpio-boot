import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40, fixed: 'left' },
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  { title: '工艺路线编码', field: 'routCode' },
  { title: '工艺路线名称', field: 'routName' },
  { title: '工艺路线说明', field: 'routDesc' },
  { title: '是否启用', field: 'status', formatter: 'dictText' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'routCode',
    title: '工艺路线编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'routName',
    title: '工艺路线名称',
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

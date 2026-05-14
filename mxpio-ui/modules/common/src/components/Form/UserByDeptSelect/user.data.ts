import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '用户账号',
    field: 'username',
  },
  {
    title: '用户姓名',
    field: 'nickname',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'username',
    title: '用户账号',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'nickname',
    title: '用户姓名',
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

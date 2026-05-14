import { BasicColumn, FormSchema } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
    width: 160,
    align: 'left',
    sorter: true,
  },
  {
    title: '用户姓名',
    dataIndex: 'nickname',
  },
  {
    title: '联系方式',
    dataIndex: 'phone',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '是否可用',
    dataIndex: 'accountNonLocked',
    customRender: ({ record }) => {
      const status = record.accountNonLocked;
      const enable = status === true;
      const color = enable ? 'green' : 'red';
      const text = enable ? '是' : '否';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '是否管理员',
    dataIndex: 'administrator',
    customRender: ({ record }) => {
      const status = record.administrator;
      const enable = status === true;
      const color = enable ? 'green' : 'red';
      const text = enable ? '是' : '否';
      return h(Tag, { color: color }, () => text);
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'nickname',
    label: '用户名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'username',
    label: '用户账号',
    component: 'Input',
    colProps: { span: 8 },
  },
  // {
  //   field: 'accountNonLocked@EQ',
  //   label: '状态',
  //   component: 'Select',
  //   componentProps: {
  //     options: [
  //       { label: '可用', value: true },
  //       { label: '停用', value: false },
  //     ],
  //   },
  //   colProps: { span: 8 },
  // },
];

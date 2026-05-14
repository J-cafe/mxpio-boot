import { BasicColumn, FormSchema } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '发送人',
    dataIndex: 'fromNickName',
    width: 160,
    align: 'left',
    sorter: true,
  },
  {
    title: '发送人账户',
    dataIndex: 'fromUserName',
  },
  {
    title: '标题',
    dataIndex: 'messageTitle',
  },
  {
    title: '阅读状态',
    dataIndex: 'readStatus',
    customRender: ({ record }) => {
      const status = record.readStatus;
      const enable = status === '1';
      const color = enable ? 'green' : 'red';
      const text = enable ? '已读' : '未读';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '接收人',
    dataIndex: 'toNickName',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'messageTitle',
    label: '标题',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'readStatus',
    label: '阅读状态',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'MB_SYSTEM_YES_NO',
    },
    colProps: { span: 8 },
  },
];

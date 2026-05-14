import { BasicColumn, FormSchema } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '任务类名',
    dataIndex: 'jobClassName',
    width: 160,
    align: 'left',
  },
  {
    title: 'cron表达式',
    dataIndex: 'jobCron',
  },
  {
    title: '参数',
    dataIndex: 'jobParams',
  },
  {
    title: '描述',
    dataIndex: 'desc',
  },
  {
    title: '状态',
    dataIndex: 'status',
    customRender: ({ record }) => {
      const status = record.status;
      const enable = status === 'RUNNING';
      const color = enable ? 'green' : 'red';
      const text = enable ? '启动' : '暂停';
      return h(Tag, { color: color }, () => text);
    },
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'jobClassName',
    label: '任务类名',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'desc',
    label: '描述',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'jobClassName',
    label: '任务类名',
    component: 'Input',
    required: true,
  },
  {
    field: 'jobCron',
    label: 'cron表达式',
    component: 'EasyCron',
    itemProps: {
      autoLink: false,
    },
    required: true,
  },
  {
    field: 'jobParams',
    label: '参数',
    component: 'InputTextArea',
  },
  {
    field: 'desc',
    label: '描述',
    component: 'InputTextArea',
  },
  {
    field: 'status',
    label: '状态',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        {
          label: '启动',
          value: 'RUNNING',
        },
        {
          label: '暂停',
          value: 'PAUSE',
        },
      ],
    },
    defaultValue: 'RUNNING',
  },
];

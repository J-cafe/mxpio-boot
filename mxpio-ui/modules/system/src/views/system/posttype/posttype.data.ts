import { BasicColumn, FormSchema } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '职系编码',
    dataIndex: 'code',
    width: 160,
    align: 'left',
    sorter: true,
  },
  {
    title: '职系名称',
    dataIndex: 'name',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'code',
    label: '职系编码',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'name',
    label: '职系名称',
    component: 'Input',
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'code',
    label: '职系编码',
    component: 'Input',
    required: true,
  },
  {
    field: 'name',
    label: '职系名称',
    component: 'Input',
    required: true,
  },
];

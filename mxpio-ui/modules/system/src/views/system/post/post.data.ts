import { BasicColumn, FormSchema } from '@mxpio/components';
import { posttypeList } from '@mxpio/api';

export const columns: BasicColumn[] = [
  {
    title: '岗位名称',
    dataIndex: 'name',
    width: 160,
    align: 'left',
    sorter: true,
  },
  {
    title: '职系',
    dataIndex: 'postType',
  },
];

export const searchFormSchema: FormSchema[] = [
  {
    field: 'name',
    label: '岗位名称',
    component: 'Input',
    colProps: { span: 8 },
  },
  {
    field: 'postType',
    label: '职系',
    component: 'ApiSelect',
    componentProps: {
      api: posttypeList,
      resultField: 'content',
      labelField: 'name',
      valueField: 'code',
      immediate: true,
    },
    colProps: { span: 8 },
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '岗位名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'postType',
    label: '职系',
    component: 'ApiSelect',
    componentProps: {
      api: posttypeList,
      resultField: 'content',
      labelField: 'name',
      valueField: 'code',
    },
    required: true,
  },
];

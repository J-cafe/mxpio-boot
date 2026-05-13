import { BasicColumn, FormSchema } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '角色名称',
    dataIndex: 'name',
    width: 160,
    align: 'left',
  },
  {
    title: '部门描述',
    dataIndex: 'description',
  },
];

export const userColumns: BasicColumn[] = [
  {
    title: '用户账号',
    dataIndex: 'username',
  },
  {
    title: '用户姓名',
    dataIndex: 'nickname',
  },
];

export const deptColumns: BasicColumn[] = [
  {
    title: '部门编码',
    dataIndex: 'deptCode',
  },
  {
    title: '部门名称',
    dataIndex: 'deptName',
  },
];

export const postColumns: BasicColumn[] = [
  {
    title: '岗位名称',
    dataIndex: 'name',
  },
  {
    title: '职系',
    dataIndex: 'postType',
  },
];

export const formSchema: FormSchema[] = [
  {
    field: 'name',
    label: '角色名称',
    component: 'Input',
    required: true,
  },
  {
    field: 'description',
    label: '描述',
    component: 'InputTextArea',
  },
];

export const datafilterColumns: BasicColumn[] = [
  {
    title: '权限范围',
    dataIndex: 'dataScope',
  },
  {
    title: '描述',
    dataIndex: 'description',
  },
];

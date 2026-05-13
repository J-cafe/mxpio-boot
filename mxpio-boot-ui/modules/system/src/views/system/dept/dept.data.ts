import { BasicColumn } from '@mxpio/components';

export const columns: BasicColumn[] = [
  {
    title: '部门名称',
    dataIndex: 'deptName',
    width: 160,
    align: 'left',
  },
  {
    title: '部门编码',
    dataIndex: 'deptCode',
  },

  {
    title: '部门级别',
    dataIndex: 'deptLevel',
    width: 100,
  },
  {
    title: '部门类型',
    dataIndex: 'deptType',
    width: 100,
  },
  {
    title: '部门负责人',
    dataIndex: 'deptManager',
    width: 100,
  },
  {
    title: '部门描述',
    dataIndex: 'deptDesc',
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

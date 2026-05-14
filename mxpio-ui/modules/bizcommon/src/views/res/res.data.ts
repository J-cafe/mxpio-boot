import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '类别编码', field: 'typeCode' },
  { title: '类别名称', field: 'typeName' },
  { title: '类别实体', field: 'typeClass' },
  { title: '单据流程', field: 'typeFlow' },
  { title: '单号前缀', field: 'typePrefix' },
  { title: '序列长度', field: 'typeSize' },
  { title: '单据模板', field: 'typeTemplate' },
  { title: '导入模板', field: 'importTemplate' },
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
    title: '类别编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'typeName',
    title: '类别名称',
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

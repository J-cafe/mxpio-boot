import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '检测方案编号', field: 'code' },
  { title: '检测方案名称', field: 'name' },
  { title: '审核状态', field: 'bpmnStatus', formatter: 'dictText' },
  { title: '适用业务类型', field: 'busiType', formatter: 'dictText' },
  { title: '方案说明', field: 'description' },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'code',
    title: '方案编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'name',
    title: '方案名称',
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

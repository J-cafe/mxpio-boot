import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '工序段编码', field: 'procGroupCode' },
  { title: '工序段名称', field: 'procGroupName' },
  { title: '工序段说明', field: 'procGroupDesc' },
  { title: '工序段类型', field: 'procGroupType', formatter: 'dictText' },
  { title: '是否启用', field: 'status', formatter: 'dictText' },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'procGroupCode',
    title: '工序段编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'procGroupName',
    title: '工序段名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'procGroupType@EQ',
    title: '工序段类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_PROC_GROUP_TYPE',
      },
    },
    span: 8,
  },
  {
    span: 8,
    className: '!pr-0',
    collapseNode: true,
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

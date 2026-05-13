import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '工序编码', field: 'processCode' },
  { title: '工序名称', field: 'processName' },
  { title: '工序分类', field: 'processClass', formatter: 'dictText' },
  { title: '工序类型', field: 'processType', formatter: 'dictText' },
  { title: '所属车间', field: 'wkshopCode', formatter: 'dictText' },
  { title: '报工方式', field: 'reportMode', formatter: 'dictText' },
  { title: '是否自动报工', field: 'autoReportAble', formatter: 'dictText' },
  { title: '允差范围', field: 'toleranceRange', formatter: 'dictText' },
  { title: '是否委外', field: 'outsourceAble', formatter: 'dictText' },
  { title: '是否启用', field: 'status', formatter: 'dictText' },
  { title: '工艺描述', field: 'craftDesc' },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'processCode',
    title: '工序编码',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'processName',
    title: '工序名称',
    itemRender: {
      name: 'AInput',
    },
    span: 8,
  },
  {
    field: 'processType@EQ',
    title: '工序类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_PROC_TYPE',
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

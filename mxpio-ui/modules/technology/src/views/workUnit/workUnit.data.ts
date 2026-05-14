import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '作业单元编码',
    field: 'workUnitCode',
    width: 120,
    sortable: true,
  },
  {
    title: '作业单元名称',
    field: 'workUnitName',
  },
  {
    title: '所属车间',
    field: 'mainWorkshop',
    formatter: 'dictText',
  },
  {
    title: '作业单元类型',
    field: 'workUnitType',
    formatter: 'dictText',
  },
  {
    title: '所属工作中心',
    field: 'workCenterCode',
    formatter: 'dictText',
  },
  {
    title: '工序名称',
    field: 'processName',
  },
  {
    title: '报工方式',
    field: 'reportMode',
    formatter: 'dictText',
  },
  {
    title: '是否启用',
    field: 'status',
    formatter: 'dictText',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'workUnitCode',
    title: '作业单元编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workUnitName',
    title: '作业单元名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workUnitType@EQ',
    title: '作业单元类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'workCenterCode@EQ',
    title: '所属工作中心',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_MES_PROC_MODE',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'processName',
    title: '工序名称',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'reportMode@EQ',
    title: '报工方式',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_REPORT_MODE',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    span: 6,
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

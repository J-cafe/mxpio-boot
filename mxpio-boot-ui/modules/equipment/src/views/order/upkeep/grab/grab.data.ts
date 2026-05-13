import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '工单编码', field: 'bizNo', width: 150 },
  { title: '资产编码', field: 'targetId', width: 100 },
  { title: '资产名称', field: 'targetName', width: 100 },
  { title: '父级资产编码', field: 'parentTargetCode', width: 120 },
  { title: '父级资产名称', field: 'parentTargetName', width: 120 },
  { title: '工单状态', field: 'orderStatus', formatter: 'dictText', width: 100 },
  { title: '规格型号', field: 'targetSpec', formatter: 'dictText', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '保养类型', field: 'upkeepType', formatter: 'dictText', width: 100 },
  { title: '计划保养开始时间', field: 'planStartTime', width: 120 },
  { title: '计划保养结束时间', field: 'planEndTime', width: 120 },
  { title: '工时（H）', field: 'planHourRation', width: 100 },
  { title: '保养班组', field: 'upkeepPersonGroupCode', formatter: 'dictText', width: 100 },
  { title: '保养负责人', field: 'executor', formatter: 'dictText', width: 100 },
  { title: '设备占用', field: 'pmisHold', width: 120 },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '工单编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'targetId',
    title: '资产编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'targetName',
    title: '资产名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'orderStatus@EQ',
    title: '工单状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_UPKEEPTASK_STATUS',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'targetSpec',
    title: '规格型号',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'useDeptId@EQ',
    title: '使用单位',
    itemRender: {
      name: 'DeptSelect',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'planStartTime',
    title: '计划开始时间',
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
    },
    folding: true,
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

import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'radio', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '工单编码',
    field: 'bizNo',
    width: 120,
  },
  {
    title: '点检计划编码',
    field: 'planCode',
    width: 120,
  },
  {
    title: '点检计划名称',
    field: 'planName',
    width: 120,
  },
  {
    title: '资产名称',
    field: 'targetName',
    width: 120,
  },
  {
    title: '资产编码',
    field: 'targetId',
    width: 120,
    slots: { default: 'detail' },
  },
  { title: '父级资产编码', field: 'parentTargetId', width: 120 },
  { title: '父级资产名称', field: 'parentTargetName', width: 120 },
  {
    title: '规格型号',
    field: 'targetSpec',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '使用单位',
    field: 'useDeptId',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '设备分类',
    field: 'eqpTypeId',
    formatter: 'dictText',
    width: 120,
  },

  {
    title: '设备等级',
    field: 'codeAbc',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '工单状态',
    field: 'orderStatus',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '计划开始时间',
    field: 'planStartTime',
    width: 150,
  },
  {
    title: '失效截止时间',
    field: 'expCutOffTime',
    width: 150,
  },
  {
    title: '是否失效',
    field: 'whetherInvalid',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '自动下达',
    field: 'isAutoRelease',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '计划下达时间',
    field: 'planReleaseTime',
    width: 150,
  },
  {
    title: '实际开始时间',
    field: 'actualStartTime',
    width: 150,
  },
  {
    title: '实际结束时间',
    field: 'actualEndTime',
    width: 150,
  },
  {
    title: '否决原因',
    field: 'reasonsRej',
    width: 150,
  },
  {
    title: '点检负责人',
    field: 'executor',
    formatter: 'dictText',
    width: 120,
  },
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
    field: 'planCode',
    title: '点检计划编码',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'planName',
    title: '点检计划名称',
    itemRender: {
      name: 'AInput',
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

import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'radio',
    width: 40,
  },
  { type: 'seq', width: 60 },
  { title: '工单编码', field: 'bizNo', width: 150 },
  { title: '紧急级别', field: 'bpmnSortFlag', slots: { default: 'bpmnSortFlag' }, width: 100 },
  { title: '维修范围', field: 'bizType', formatter: 'dictText', width: 100 },
  { title: '维修区域', field: 'targetId', width: 150, formatter: 'dictText' },
  { title: '工单状态', field: 'repairStatus', formatter: 'dictText', width: 100 },
  { title: '故障描述', field: 'existingProblems', width: 100 },
  // { title: '故障日期', field: 'failureDate', width: 100 },
  { title: '申请人', field: 'applyId', width: 100, formatter: 'dictText' },
  { title: '申请部门', field: 'applyDept', width: 100, formatter: 'dictText' },
  { title: '维修人', field: 'distributePersonId', formatter: 'dictText', width: 120 },
  { title: '实际开始时间', field: 'actualStartDate', width: 180 },
  { title: '实际结束时间', field: 'actualEndDate', width: 180 },
  { title: '是否逾期', field: 'overFlag', width: 100 },
  { title: '是否委外', field: 'isOutsource', formatter: 'dictText', width: 100 },
  { title: '维修工时(分钟)', field: 'workMinutes', width: 120 },
  { title: '超出时长(分钟)', field: 'goBeyondMinutes', width: 120 },
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
    field: 'applyId@IN',
    title: '申请人',
    itemRender: {
      name: 'UserByDeptSelect',
    },
    span: 6,
  },
  {
    field: 'failureDate',
    title: '故障日期',
    itemRender: {
      name: 'ADatePicker',
      props: {
        format: 'yyyy-MM-dd',
      },
      attrs: {
        class: 'w-full',
      },
    },
    span: 6,
  },
  {
    field: 'repairStatus@EQ',
    title: '工单状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_OTHER_REPAIR_STATUS',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'isOutsource@EQ',
    title: '是否委外',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_YESNO',
      },
    },
    folding: true,
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
    collapseNode: true,
  },
];

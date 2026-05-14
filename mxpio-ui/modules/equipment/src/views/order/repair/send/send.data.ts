import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '工单编码', field: 'bizNo', width: 150 },
  { title: '工单类型', field: 'bizType', formatter: 'dictText', width: 100 },
  { title: '资产编码', field: 'targetId', width: 100 },
  { title: '资产名称', field: 'targetName', width: 100 },
  { title: '工单状态', field: 'repairStatus', formatter: 'dictText', width: 100 },
  { title: '故障停机', field: 'applyHalt', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '父级资产编码', field: 'parentTargetCode', width: 120 },
  { title: '父级资产名称', field: 'parentTargetName', width: 120 },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '故障日期', field: 'failureDate', width: 100 },
  { title: '报修人', field: 'applyId', width: 100, formatter: 'dictText' },
  { title: '联系电话', field: 'chkPersonPhone', width: 100 },
  { title: '故障描述', field: 'existingProblems', width: 100 },
  { title: '申请时间', field: 'applyTime', width: 100 },
  { title: '源单单号', field: 'sourceBizNo', width: 100 },
  { title: '是否瓶颈设备', field: 'isBottleneck', formatter: 'dictText', width: 120 },
  { title: '数据来源类别', field: 'sourceType', width: 100 },
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
    field: 'bizType@EQ',
    title: '工单类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_REPAIR_BIZ_TYPE',
      },
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
    folding: true,
    span: 6,
  },
  {
    field: 'applyId@EQ',
    title: '报修人',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'failureTime',
    title: '故障日期',
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
    field: 'repairStatus@EQ',
    title: '工单状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_REPAIR_STATUS',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'isBottleneck@EQ',
    title: '是否瓶颈设备',
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

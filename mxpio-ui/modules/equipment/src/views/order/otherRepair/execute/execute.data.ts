import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import XEUtils from 'xe-utils';
import { useMessage } from '@mxpio/hooks';

const { createMessage } = useMessage();

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'radio',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
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
  { title: '是否逾期', field: 'overFlag', width: 100, formatter: 'dictText' },
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

export const progressColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
  },
  { title: '人员', field: 'reporter', formatter: 'dictText' },
  { title: '时间', field: 'reportTime' },
  { title: '状态', field: 'status', formatter: 'dictText' },
  { title: '内容', field: 'content' },
];

export const pickingColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
  },
  { title: '行号', field: 'lineNo', width: '50' },
  { title: '物料编码', field: 'itemCode' },
  { title: '物料名称', field: 'itemName' },
  { title: '物料分类', field: 'category', formatter: 'dictText' },
  { title: '规格型号', field: 'itemSpec' },
  { title: '订货号', field: 'orderNo' },
  { title: '单位', field: 'unitCode', formatter: 'dictText' },
  { title: '数量', field: 'quantity' },
  { title: '实际领料数', field: 'actualQuantity' },
  { title: '计划领料数', field: 'planQuantity' },
  { title: '实际退料数', field: 'actualRejectQuantity' },
  { title: '计划退料数', field: 'planRejectQuantity' },
];

export const returnColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 140,
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 120,
  },
  {
    title: '规格型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 80,
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '数量',
    field: 'quantity',
    width: 80,
  },
  {
    title: '最大退货数',
    field: 'maxQuantity',
    width: 100,
  },
  {
    title: '退货数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: {
        disabled: true,
      },
    },
    width: 120,
  },
  {
    title: '批次号',
    field: 'lotNo',
    editRender: {
      name: 'LotExecutedSelect',
      events: {
        change: (params: any, value, items) => {
          const { row } = params;
          const executeQuantity = items.reduce((total: number, item: any) => {
            return total + (Number(item.executeQuantity) || 0);
          }, 0);
          XEUtils.set(row, 'executeQuantity', executeQuantity);
        },
      },
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        filters: {
          whCode: row.whCode,
        },
        bizNo: row.bizNo,
        lineNo: row.lineNo,
        accessType: 'out',
        beforeSubmit: async (value, selectRows) => {
          const total = selectRows.reduce((total: number, item: any) => {
            return total + (Number(item.executeQuantity) || 0);
          }, 0);
          if (total > Number(row.maxQuantity)) {
            createMessage.error('退货数量合计不能大于最大数量');
            return Promise.reject('退货数量合计不能大于最大数量');
          }
        },
      }),
    },
    width: 120,
  },
];

export const transferColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
  },
  {
    title: '转单类型',
    field: 'type',
  },
  {
    title: '原执行者',
    field: 'oldExecutor',
  },
  {
    title: '现执行者',
    field: 'newExecutor',
  },
  {
    title: '转单时间',
    field: 'transferTime',
  },
  {
    title: '变更原因',
    field: 'reason',
  },
];

export const assistColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
  },
  { title: '姓名', field: 'personName' },
  { title: '开始时间', field: 'startTime' },
  { title: '结束时间', field: 'endTime' },
  { title: '工时', field: 'manHour' },
  { title: '协助内容', field: 'content' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 120,
  },
];

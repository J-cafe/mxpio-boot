import type { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import XEUtils from 'xe-utils';
import { useMessage } from '@mxpio/hooks';
import { dateUtil } from '@mxpio/utils';

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
  { title: '资产编码', field: 'targetId', width: 150 },
  { title: '资产名称', field: 'targetName', width: 100 },
  { title: '父级资产编码', field: 'parentTargetCode', width: 120 },
  { title: '父级资产名称', field: 'parentTargetName', width: 120 },
  { title: '工单状态', field: 'orderStatus', formatter: 'dictText', width: 100 },
  { title: '是否委外', field: 'isOutsource', formatter: 'dictText', width: 100 },
  { title: '是否异常', field: 'isAbnormal', formatter: 'dictText', width: 100 },
  { title: '规格型号', field: 'targetSpec', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '保养类型', field: 'upkeepType', formatter: 'dictText', width: 100 },
  { title: '计划保养开始时间', field: 'planStartTime', width: 120 },
  { title: '计划保养结束时间', field: 'planEndTime', width: 120 },
  { title: '实际保养开始时间', field: 'actualStartTime', width: 120 },
  { title: '实际保养结束时间', field: 'actualEndTime', width: 120 },
  { title: '工时（H）', field: 'planHourRation', width: 100 },
  {
    title: '保养工时（H）',
    field: 'workMinutes',
    width: 100,
    formatter: (value) => {
      if (value.cellValue) {
        return dateUtil.duration(value.cellValue, 'minutes').asHours().toFixed(2);
      } else if (value.cellValue === 0) {
        return 0;
      }
      return '';
    },
  },
  { title: '是否逾期', field: 'overFlag', formatter: 'dictText', width: 100 },
  { title: '保养班组', field: 'upkeepPersonGroupCode', formatter: 'dictText', width: 100 },
  { title: '工程师组', field: 'engineerPersonGroupCode', formatter: 'dictText', width: 100 },
  { title: '保养负责人', field: 'executor', formatter: 'dictText', width: 100 },
  { title: '设备占用', field: 'pmisHold', width: 100, formatter: 'dictText' },
  { title: '是否推送', field: 'isPush', formatter: 'dictText', width: 100 },
  // { title: '保养附件', field: 'attachFile', slots: { default: 'attachFile' }, width: 100 },
  { title: '计划名称', field: 'planName', width: 120 },
  { title: '创建日期', field: 'createTime', width: 120 },
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
  { title: '原执行者', field: 'oldExecutor', formatter: 'dictText' },
  { title: '现执行者', field: 'newExecutor', formatter: 'dictText' },
  { title: '转单时间', field: 'transferTime' },
  { title: '变更原因', field: 'reason' },
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

export const executeDetailColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '内容',
    field: 'content',
    width: 120,
  },
  {
    title: '部位描述',
    field: 'placeDescribe',
    width: 120,
  },
  {
    title: '方法',
    field: 'method',
    width: 100,
  },
  {
    title: '目标',
    field: 'target',
    width: 100,
  },
  {
    title: '是否记录属性',
    field: 'isRecord',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '上限值',
    field: 'upperLimit',
    width: 100,
  },
  {
    title: '下限值',
    field: 'lowerLimit',
    width: 100,
  },
  {
    title: '内容类型',
    field: 'contentType',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '存在选项',
    align: 'center',
    field: 'chooseEnable',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '告警阈值',
    field: 'warnValue',
    width: 100,
  },
  {
    title: '预警阈值',
    field: 'earlyWarnValue',
    width: 100,
  },
  {
    title: '属性单位',
    field: 'attributeUnit',
    width: 100,
  },
  {
    title: '估计用时（分钟）',
    field: 'evaluateTime',
    width: 100,
  },
  {
    title: '标准图片',
    field: 'pic',
    slots: { default: 'pic' },
    width: 100,
  },
  {
    title: '实际值',
    field: 'actualVaule',
    editRender: {},
    slots: { default: 'actualVaule', edit: 'actualVaule' },
    width: 150,
    fixed: 'right',
  },
  {
    title: '是否异常',
    field: 'isAbnormal',
    editRender: {
      name: 'DictSelect',
      props: { dictCode: 'ERP_COMMON_YESNO' },
    },
    width: 120,
    fixed: 'right',
  },
  {
    title: '异常描述',
    field: 'abnormalRemark',
    editRender: { name: 'AInput' },
    width: 150,
  },
];

export const rateDetailColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '评价内容', field: 'content', align: 'left', headerAlign: 'center' },
  { title: '评分', field: 'score', editRender: { name: 'ARate' } },
  { title: '备注', field: 'desc', editRender: { name: 'AInput' } },
];

export const rateColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '评价内容', field: 'content', align: 'left', headerAlign: 'center' },
  { title: '评分', field: 'score' },
  { title: '备注', field: 'desc' },
];

export const remarkColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  { title: '备注内容', field: 'remark' },
  { title: '填写人', field: 'fillPeople', formatter: 'dictText' },
  { title: '填写日期', field: 'fillDate' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const remarkFormSchema: FormSchema[] = [
  {
    field: 'remark',
    label: '备注内容',
    component: 'InputTextArea',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillPeople',
    label: '填报人',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillDate',
    label: '填报时间',
    component: 'DatePicker',
    colProps: {
      span: 24,
    },
    componentProps: {
      class: '!w-full',
    },
  },
];

import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import XEUtils from 'xe-utils';
import Big from 'big.js';
import { useMessage } from '@mxpio/hooks';

const { createMessage } = useMessage();

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40, fixed: 'left' },
  { type: 'seq', width: 60, fixed: 'left' },
  { title: '需求单号', field: 'bizNo', width: 120, fixed: 'left' },
  { title: '审批状态', field: 'organizationDemand.textMap.bpmnStatus$DICT_TEXT_', width: 100 },
  { title: '执行状态', field: 'organizationDemand.textMap.orderStatus$DICT_TEXT_', width: 100 },
  { title: '关闭状态', field: 'organizationDemand.textMap.closeStatus$DICT_TEXT_', width: 100 },
  { title: '行号', field: 'lineNo', width: 60 },
  { title: '行状态', field: 'closeStatus', width: 100, formatter: 'dictText' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 100 },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '需求数量', field: 'quantity', width: 100 },
  { title: '需求日期', field: 'demandDate', width: 100, sortable: true },
  {
    title: '最大可领料数',
    field: 'actualMaxQuantity',
    width: 120,
    slots: {
      default: ({ row }) => {
        row.actualMaxQuantity = Big(row.quantity || 0)
          .minus(row.actualQuantity || 0)
          .minus(row.planQuantity || 0)
          .plus(row.actualRejectQuantity || 0)
          .toNumber();
        return row.actualMaxQuantity >= 0 ? row.actualMaxQuantity : '0';
      },
    },
  },
  { title: '实际领料数', field: 'actualQuantity', width: 100 },
  { title: '计划领料数', field: 'planQuantity', width: 100 },
  { title: '实际退料数', field: 'actualRejectQuantity', width: 100 },
  { title: '计划退料数', field: 'planRejectQuantity', width: 100 },
  { title: '创建人', field: 'createBy', width: 100, formatter: 'dictText' },
  { title: '备注', field: 'memo', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '需求单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemName',
    title: '物料名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'drawingNo',
    title: '图号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'createBy@IN',
    title: '创建人',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: true,
      },
    },
    span: 6,
  },
  {
    field: 'closeStatus@IN',
    title: '行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
        mode: 'multiple',
      },
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

export const pickingColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '行号', field: 'lineNo', width: 70, sortable: true },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '数量', field: 'quantity', width: 80 },
  {
    title: '材料类型',
    field: 'materialType',
    formatter: 'dictText',
    width: 100,
  },
  { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 100 },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '最大领料数', field: 'maxQuantity', width: 120 },
  {
    title: '领料数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: row.selectLot?.length > 0 || row.maxQuantity <= 0,
      }),
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '仓库',
    field: 'whCode',
    editRender: {
      name: 'WareHouseSelect',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: !!row.lotNo || row.maxQuantity <= 0,
        filters: { 'whType@EQ': '1' },
      }),
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '批次号',
    field: 'lotNo',
    editRender: {
      name: 'InvLotSelect',
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
          itemCode: row.itemCode,
        },
        disabled: row.maxQuantity <= 0 || row.appointLotNo,
        beforeSubmit: async (value, selectRows) => {
          const total = selectRows.reduce((total: number, item: any) => {
            return total + (Number(item.executeQuantity) || 0);
          }, 0);
          if (total > Number(row.maxQuantity)) {
            createMessage.error('领料数量合计不能大于最大领料数');
            return Promise.reject('领料数量合计不能大于最大领料数');
          }
        },
      }),
    },
    width: 150,
    fixed: 'right',
  },
];

export const supplementColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '行号', field: 'lineNo', width: 70, sortable: true },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '数量', field: 'quantity', width: 80 },
  { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
  {
    title: '材料类型',
    field: 'materialType',
    formatter: 'dictText',
    width: 100,
  },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '下料长度', field: 'makeLength', width: 100 },
  { title: '可制数量', field: 'makeNum', width: 100 },
  {
    title: '补料原因',
    field: 'supplementReason',
    editRender: { name: 'AInput' },
    width: 150,
    fixed: 'right',
  },
  {
    title: '补料数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: row.selectLot?.length > 0 || row.maxQuantity <= 0,
      }),
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '仓库',
    field: 'whCode',
    editRender: {
      name: 'WareHouseSelect',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: !!row.lotNo,
      }),
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '批次号',
    field: 'lotNo',
    editRender: {
      name: 'InvLotSelect',
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
          itemCode: row.itemCode,
        },
      }),
    },
    width: 150,
    fixed: 'right',
  },
];

export const returnColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '行号', field: 'lineNo', width: 70, sortable: true },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '数量', field: 'quantity', width: 80 },
  { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
  {
    title: '材料类型',
    field: 'materialType',
    formatter: 'dictText',
    width: 100,
  },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '下料长度', field: 'makeLength', width: 100 },
  { title: '可制数量', field: 'makeNum', width: 100 },
  { title: '最大退料数', field: 'maxQuantity', width: 120 },
  {
    title: '退料数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: {
        disabled: true,
      },
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '仓库',
    field: 'whCode',
    editRender: {
      name: 'WareHouseSelect',
      props: {
        disabled: true,
      },
    },
    width: 150,
    fixed: 'right',
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
            createMessage.error('退料数量合计不能大于最大退料数量');
            return Promise.reject('退料数量合计不能大于最大退料数量');
          }
        },
      }),
    },
    width: 150,
    fixed: 'right',
  },
];

export const scrapColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '行号', field: 'lineNo', width: 70, sortable: true },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '数量', field: 'quantity', width: 80 },
  { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
  {
    title: '材料类型',
    field: 'materialType',
    formatter: 'dictText',
    width: 100,
  },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '下料长度', field: 'makeLength', width: 100 },
  { title: '可制数量', field: 'makeNum', width: 100 },
  { title: '最大退料数', field: 'maxQuantity', width: 120 },
  {
    title: '是否补料',
    field: 'supplementFlag',
    editRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
    },
    width: 120,
    fixed: 'right',
  },
  {
    title: '退料数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: {
        disabled: true,
      },
    },
    width: 120,
    fixed: 'right',
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
            createMessage.error('退料数量合计不能大于最大退料数量');
            return Promise.reject('退料数量合计不能大于最大退料数量');
          }
        },
      }),
    },
    width: 150,
    fixed: 'right',
  },
  {
    title: '退料接收仓库',
    field: 'whCode',
    editRender: {
      name: 'WareHouseSelect',
      props: {
        filters: { whType: '4' },
      },
    },
    width: 120,
    fixed: 'right',
  },
  {
    title: '异常原因',
    field: 'defectiveReason',
    editRender: { name: 'AInput' },
    fixed: 'right',
    width: 150,
  },
  { title: '发现人', field: 'reporter', editRender: { name: 'AInput' }, width: 120 },
  { title: '发现工序', field: 'reportProc', editRender: { name: 'AInput' }, width: 120 },
];

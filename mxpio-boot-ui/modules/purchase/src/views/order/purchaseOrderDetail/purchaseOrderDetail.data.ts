import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import XEUtils from 'xe-utils';
import Big from 'big.js';
import { useMessage } from '@mxpio/hooks';

const { createMessage } = useMessage();

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40, fixed: 'left' },
  { title: '序号', type: 'seq', width: '50', align: 'center', fixed: 'left' },
  { title: '供应商编码', field: 'purchaseOrder.pnCode', width: 100, fixed: 'left' },
  { title: '供应商', field: 'purchaseOrder.pnName', width: 100, fixed: 'left' },
  { title: '采购单号', field: 'bizNo', width: 120, fixed: 'left' },
  { title: '审批状态', field: 'purchaseOrder.textMap.bpmnStatus$DICT_TEXT_', width: 100 },
  { title: '执行状态', field: 'purchaseOrder.textMap.orderStatus$DICT_TEXT_', width: 100 },
  { title: '关闭状态', field: 'purchaseOrder.textMap.closeStatus$DICT_TEXT_', width: 100 },
  { title: '行号', field: 'lineNo', width: 60 },
  { title: '行状态', field: 'closeStatus', width: 100, formatter: 'dictText' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100, slots: { default: 'drawingNo' } },
  { title: '物料组', field: 'itemGroupCode', width: 100, formatter: 'dictText' },
  { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
  { title: '采购数量', field: 'quantity', width: 80 },
  { title: '到货日期', field: 'suggestArriveDate', width: 120, sortable: true },
  {
    title: '最大可收货数',
    field: 'actualMaxQuantity',
    width: 120,
    slots: {
      default: ({ row }) => {
        row.actualMaxQuantity = Big(row.quantity || 0)
          .minus(row.actualQuantity || 0)
          .minus(row.planQuantity || 0)
          .plus(row.actualRejectQuantity || 0)
          .toNumber();
        return row.actualMaxQuantity;
      },
    },
  },
  {
    title: '实际收货数',
    field: 'actualQuantity',
    width: 100,
    slots: {
      default: 'actualQuantity',
    },
  },
  { title: '计划收货数', field: 'planQuantity', width: 100 },
  { title: '实际退货数', field: 'actualRejectQuantity', width: 100 },
  { title: '计划退货数', field: 'planRejectQuantity', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '采购单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'pnName',
    title: '供应商',
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
    folding: true,
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
    field: 'bizMan@IN',
    title: '业务员',
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
    field: 'orderDate@EQ',
    title: '单据日期',
    folding: true,
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'yyyy-MM-dd',
      },
      attrs: {
        class: '!w-full',
      },
    },
    span: 6,
  },

  {
    field: 'orderType@IN',
    title: '单据类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_PURC_ORDER_TYPE',
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

export const receiveColumns: VxeGridPropTypes.Columns = [
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
    title: '最大到货数',
    field: 'maxQuantity',
    width: 100,
  },
  {
    title: '到货数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: !!row.lotNo || row.maxQuantity <= 0,
      }),
    },
    width: 120,
  },
  {
    title: '仓库',
    field: 'whCode',
    editRender: {
      name: 'WareHouseSelect',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: !!row.lotNo || row.maxQuantity <= 0,
      }),
    },
    width: 120,
  },
];

export const returnColumns: VxeGridPropTypes.Columns = [
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
        accessType: 'in',
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

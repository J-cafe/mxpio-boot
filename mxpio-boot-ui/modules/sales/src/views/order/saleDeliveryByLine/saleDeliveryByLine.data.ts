import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import XEUtils from 'xe-utils';
import Big from 'big.js';
import { useMessage } from '@mxpio/hooks';

const { createMessage } = useMessage();

export const columns: VxeGridPropTypes.Columns = [
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
    title: '客户编码',
    field: 'salesOrder.pnCode',
    width: 100,
  },
  {
    title: '客户名称',
    field: 'salesOrder.pnName',
    width: 100,
  },
  {
    title: '销售单号',
    field: 'bizNo',
    width: 100,
  },
  {
    title: '审批状态',
    field: 'salesOrder.textMap.bpmnStatus$DICT_TEXT_',
    width: 80,
  },
  {
    title: '执行状态',
    field: 'salesOrder.textMap.orderStatus$DICT_TEXT_',
    width: 80,
  },
  {
    title: '关闭状态',
    field: 'salesOrder.textMap.closeStatus$DICT_TEXT_',
    width: 80,
  },
  {
    title: '行号',
    field: 'lineNo',
    width: 60,
  },
  {
    title: '行状态',
    field: 'closeStatus',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 100,
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 100,
  },
  {
    title: '规格型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 100,
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '订单数量',
    field: 'quantity',
    width: 80,
  },
  {
    title: '交付日期',
    field: 'deliverDate',
    width: 100,
  },
  {
    title: '最大可发货数',
    field: 'actualMaxQuantity',
    width: 100,
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
    title: '实际发货数',
    field: 'actualQuantity',
    width: 100,
  },
  {
    title: '计划发货数',
    field: 'planQuantity',
    width: 100,
  },
  {
    title: '实际退货数',
    field: 'actualRejectQuantity',
    width: 100,
  },
  {
    title: '计划退货数',
    field: 'planRejectQuantity',
    width: 100,
  },
  {
    title: '实时库存',
    field: 'liveStockQuantity',
    width: 100,
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '备注',
    field: 'memo',
    width: 100,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
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
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'bizNo',
    title: '销售单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'deliverDate',
    title: '交付日期',
    folding: true,
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'yyyy-MM-dd',
      },
      attrs: {
        class: 'w-full',
      },
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

export const deliveryColumns: VxeGridPropTypes.Columns = [
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
    title: '最大发货数',
    field: 'maxQuantity',
    width: 100,
  },
  {
    title: '发货数量',
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
        beforeSubmit: async (value, selectRows) => {
          const total = selectRows.reduce((total: number, item: any) => {
            return total + (Number(item.executeQuantity) || 0);
          }, 0);
          if (total > Number(row.maxQuantity)) {
            createMessage.error('出库数量合计不能大于最大执行数量');
            return Promise.reject('执行数量不能大于最大执行数量');
          }
        },
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
  // {
  //   title: '仓库',
  //   field: 'whCode',
  //   editRender: {
  //     name: 'WareHouseSelect',
  //     props: {
  //       disabled: true,
  //     },
  //   },
  //   width: 120,
  // },
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

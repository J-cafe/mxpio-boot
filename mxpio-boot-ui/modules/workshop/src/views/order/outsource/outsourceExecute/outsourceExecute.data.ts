import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';
import XEUtils from 'xe-utils';
import { useMessage } from '@mxpio/hooks';

const { createMessage } = useMessage();
export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center' },
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  { title: '委外订单号', field: 'bizNo', width: 100 },
  { title: '订单类型', field: 'orderType', width: 100, formatter: 'dictText' },
  { title: '加工商编码', field: 'pnCode', width: 100 },
  { title: '加工商名称', field: 'pnName', width: 100 },
  { title: '业务员', field: 'bizMan', formatter: 'dictText', width: 100 },
  { title: '计划开工日期', field: 'startDate', width: 120 },
  { title: '计划完工日期', field: 'endDate', width: 120 },
  { title: '执行状态', field: 'orderStatus', width: 100, formatter: 'dictText' },
  { title: '关闭状态', field: 'closeStatus', width: 100, formatter: 'dictText' },
  { title: '物料编码', field: 'productItemCode', width: 120 },
  { title: '物料名称', field: 'productItemName', width: 120 },
  { title: '规格型号', field: 'itemSpec', width: 120 },
  { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
  { title: '图号', field: 'productDrawingNo', width: 100 },
  { title: '订单数量', field: 'orderQuantity', width: 100 },
  { title: '实际完工数量', field: 'actualQuantity', width: 120 },
  { title: '计划完工数量', field: 'planQuantity', width: 120 },
  { title: '参考日期', field: 'referenceDate', width: 100 },
  { title: '加工商', field: 'pnName', width: 100 },
  { title: '加工要求', field: 'memo', width: 100 },
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
    title: '委外订单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'productItemCode',
    title: '产品编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'pnCode',
    title: '加工商',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'productDrawingNo',
    title: '图号',
    folding: true,
    itemRender: {
      name: 'AInput',
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
        dictCode: 'ERP_OO_ORDER_TYPE',
        multiple: true,
      },
    },
    span: 6,
  },
  {
    field: 'orderStatus@IN',
    title: '执行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
        multiple: true,
      },
    },
    span: 6,
  },
  {
    field: 'closeStatus@IN',
    title: '关闭状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
        multiple: true,
      },
    },
    span: 6,
  },
  {
    field: 'startDate',
    title: '计划开工日期',
    folding: true,
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
      },
    },
    span: 6,
  },
  {
    field: 'endDate',
    title: '计划完工日期',
    folding: true,
    itemRender: {
      name: 'ARangePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        autoFormat: true,
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

export const bomFormSchema: VxeFormItemProps[] = [
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
    title: '齐套数量',
    field: 'analysisColor',
    width: 100,
    slots: {
      default: ({ row }) => {
        const color = row.analysisColor;
        if (!row.analysisColor) {
          return '';
        }
        return h(Tag, { color: color }, () => row.kittingQuantity);
      },
    },
  },
  { title: '最大领料数', field: 'maxQuantity', width: 120 },
  { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
  {
    title: '材料类型',
    field: 'materialType',
    cellRender: { name: 'ZDictCell', props: { dictCode: 'ERP_TECH_MATERIAL_TYPE' } },
    width: 100,
  },
  { title: '材料牌号', field: 'materialBrand', width: 100 },
  { title: '下料长度', field: 'makeLength', width: 100 },
  { title: '可制数量', field: 'makeNum', width: 100 },
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
  {
    title: '齐套数量',
    field: 'analysisColor',
    width: 100,
    slots: {
      default: ({ row }) => {
        const color = row.analysisColor;
        if (!row.analysisColor) {
          return '';
        }
        return h(Tag, { color: color }, () => row.kittingQuantity);
      },
    },
  },
  { title: '供料方式', field: 'feedingMode', formatter: 'dictText', width: 100 },
  {
    title: '材料类型',
    field: 'materialType',
    cellRender: { name: 'ZDictCell', props: { dictCode: 'ERP_TECH_MATERIAL_TYPE' } },
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
  {
    title: '齐套数量',
    field: 'analysisColor',
    width: 100,
    slots: {
      default: ({ row }) => {
        const color = row.analysisColor;
        if (!row.analysisColor) {
          return '';
        }
        return h(Tag, { color: color }, () => row.kittingQuantity);
      },
    },
  },
  { title: '最大退料数', field: 'maxQuantity', width: 120 },
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
  // {
  //   title: '仓库',
  //   field: 'whCode',
  //   editRender: {
  //     name: 'WareHouseSelect',
  //     props: {
  //       disabled: true,
  //     },
  //   },
  //   width: 150,
  //   fixed: 'right',
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
  { title: '最大退料数', field: 'maxQuantity', width: 120 },
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
    width: 120,
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
    width: 150,
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

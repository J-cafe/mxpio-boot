import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import { useMessage } from '@mxpio/hooks';
import XEUtils from 'xe-utils';

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
  { title: '仓单编号', field: 'bizNo', width: 120 },
  { title: '仓库名称', field: 'whCode', width: 100, formatter: 'dictText' },
  { title: '仓单类型', field: 'warehouseOrder.textMap.woType$DICT_TEXT_', width: 100 },
  { title: '行号', field: 'lineNo', width: 80, sortable: true },
  { title: '行状态', field: 'closeStatus', width: 80, formatter: 'dictText' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '图号', field: 'drawingNo', width: 80 },
  { title: '单位', field: 'unitCode', width: 80, formatter: 'dictText' },
  { title: '数量', field: 'quantity', width: 80 },
  { title: '批次号', field: 'lotNo', width: 120 },
  { title: '已执行数量', field: 'actualQuantity', width: 120 },
  { title: '实时库存', field: 'liveStockQuantity', width: 120 },
  { title: '源单单号', field: 'originBizNo', width: 120 },
  { title: '源单行号', field: 'originBizLineNo', width: 100 },
  { title: '通知单编号', field: 'originNoticeNo', width: 100 },
  { title: '制单人', field: 'createBy', formatter: 'dictText', width: 100 },
  { title: '制单时间', field: 'createTime', width: 100 },
  { title: '制单人部门', field: 'createDept', formatter: 'dictText', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'originBizNo',
    title: '源单编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originNoticeNo',
    title: '通知单编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'whCode',
    title: '仓库',
    itemRender: {
      name: 'WareHouseSelect',
    },
    span: 6,
  },
  {
    field: 'bizNo',
    title: '单据编号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemCode',
    title: '物料编码',
    folding: true,
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

export const executeColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: 50,
    align: 'center',
  },
  { title: '仓库', field: 'whCode', width: 100, formatter: 'dictText' },
  { title: '行号', field: 'lineNo', width: 80, sortable: true },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 100,
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
  { title: '允差数量', field: 'toleranceRangeQuantity', width: 80 },
  { title: '已执行数量', field: 'actualQuantity', width: 100 },
  {
    title: '最大执行数量',
    field: 'maxQuantity',
    width: 100,
  },
  {
    title: '执行数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
      props: ({ row }) => ({
        // 根据行数据动态设置禁用状态
        disabled: !!row.lotNo || row.maxQuantity <= 0,
      }),
    },
    width: 120,
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
        beforeSubmit: async (value, selectRows) => {
          const total = selectRows.reduce((total: number, item: any) => {
            return total + (Number(item.executeQuantity) || 0);
          }, 0);
          if (total > Number(row.maxQuantity)) {
            createMessage.error('出库数量合计不能大于最大执行数量');
            return Promise.reject('执行数量不能大于最大执行数量');
          }
        },
        disabled: row.hasLot,
        filters: {
          whCode: row.whCode,
          itemCode: row.itemCode,
        },
      }),
    },
    width: 120,
    fixed: 'right',
  },
  { title: '实时库存', field: 'liveStockQuantity', width: 80, fixed: 'right' },
];

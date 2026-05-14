import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import { useMessage } from '@mxpio/hooks';
import XEUtils from 'xe-utils';

const { createMessage } = useMessage();

export const columns: VxeGridPropTypes.Columns = [
  // {
  //   type: 'checkbox',
  //   width: 40,
  // },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  {
    title: '单据编号',
    field: 'bizNo',
    width: 120,
    sortable: true,
  },
  {
    title: '单据日期',
    field: 'orderDate',
    width: 100,
  },
  {
    title: '单据类型',
    field: 'woType',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '审批状态',
    field: 'bpmnStatus',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '执行状态',
    field: 'orderStatus',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '关闭状态',
    field: 'closeStatus',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '仓库',
    field: 'whCode',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '源单编号',
    field: 'originBizNo',
    width: 120,
  },
  {
    title: '通知单编号',
    field: 'originNoticeNo',
    width: 120,
  },
  {
    title: '制单人',
    field: 'createBy',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '制单时间',
    field: 'createTime',
    width: 100,
  },
  {
    title: '审核人',
    field: 'reviewer',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '审核时间',
    field: 'reviewTime',
    width: 100,
  },
  {
    title: '执行人',
    field: 'executor',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '执行时间',
    field: 'executeTime',
    width: 100,
  },
  {
    title: '备注',
    field: 'memo',
    width: 100,
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
    field: 'bizNo',
    title: '单据编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'whCode',
    title: '仓库',
    folding: true,
    itemRender: {
      name: 'WareHouseSelect',
    },
    span: 6,
  },
  {
    field: 'bpmnStatus@EQ',
    title: '审核状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'orderStatus@EQ',
    title: '执行状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_ORDER_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'closeStatus@EQ',
    title: '关闭状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_CLOSE_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'createTime',
    title: '制单日期',
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

export const formSchema: FormSchema[] = [
  {
    field: 'bizNo',
    label: '单据编码',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'woType',
    label: '单据类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_WO_TYPE',
        disabled: true,
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderDate',
    label: '单据日期',
    required: true,
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'bpmnStatus',
    label: '审核状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_BPMN_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderStatus',
    label: '执行状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_ORDER_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'closeStatus',
    label: '关闭状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_CLOSE_STATUS',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'whCode',
    label: '仓库',
    required: true,
    component: 'WareHouseSelect',
    colProps: {
      span: 6,
    },
  },
  {
    field: 'originBizNo',
    label: '源单编码',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 6,
    },
  },
];

export const executeColumns: VxeGridPropTypes.Columns = [
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
    width: 120,
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
  // { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 80 },
  // { title: '材料牌号', field: 'materialBrand', width: 80 },
  // { title: '直径', field: 'materialDiameter', width: 80 },
  // { title: '壁厚', field: 'materialThickness', width: 80 },
  {
    title: '数量',
    field: 'quantity',
    width: 80,
  },
  { title: '允差数量', field: 'toleranceRangeQuantity', width: 100 },
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
  { title: '实时库存', field: 'liveStockQuantity', width: 120 },
];

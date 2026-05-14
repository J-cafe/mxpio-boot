import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

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
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  { title: '通知单号', field: 'noticeNo', width: 120 },
  { title: '委外订单号', field: 'bizOrderNo', width: 120 },
  { title: '单据状态', field: 'noticeStatus', width: 100, formatter: 'dictText' },
  { title: '单据日期', field: 'noticeDate', width: 100 },
  { title: '产品编码', field: 'productItemCode', width: 100 },
  { title: '产品名称', field: 'productItemName', width: 100 },
  { title: '产品图号', field: 'productDrawingNo', width: 100 },
  { title: '主制车间', field: 'mainWorkshop', formatter: 'dictText', width: 100 },
  { title: '仓库', field: 'whCode', formatter: 'dictText', width: 100 },
  { title: '创建人', field: 'createBy', formatter: 'dictText', width: 100 },
  { title: '创建时间', field: 'createTime', width: 120 },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'whCode',
    title: '仓库',
    itemRender: {
      name: 'WareHouseSelect',
    },
    span: 6,
  },
  {
    field: 'bizOrderNo',
    title: '委外订单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'noticeNo',
    title: '通知单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'noticeStatus@EQ',
    title: '单据状态',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_NOTICE_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'productItemName',
    title: '产品名称',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'productDrawingNo',
    title: '产品图号',
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

export const formSchema: FormSchema[] = [
  {
    field: 'noticeNo',
    label: '通知单号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'noticeDate',
    label: '单据日期',
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'noticeStatus',
    label: '单据状态',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_NOTICE_STATUS',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'whCode',
    label: '仓库',
    component: 'WareHouseSelect',
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'bizOrderNo',
    label: '委外订单号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'productItemCode',
    label: '产品编码',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'productItemName',
    label: '产品名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'productDrawingNo',
    label: '产品图号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'mainWorkshop',
    label: '主制车间',
    component: 'WorkShopSelect',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
];

export const executeColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
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
    width: 80,
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  { title: '材料类型', field: 'materialType', formatter: 'dictText', width: 80 },
  { title: '材料牌号', field: 'materialBrand', width: 80 },
  { title: '下料长度', field: 'makeLength', width: 80 },
  { title: '可制数量', field: 'makeNum', width: 80 },
  { title: '直径', field: 'materialDiameter', width: 80 },
  { title: '壁厚', field: 'materialThickness', width: 80 },
  {
    title: '数量',
    field: 'quantity',
    width: 80,
  },
  {
    title: '批次号',
    field: 'lotNo',
    width: 120,
  },
  {
    title: '批次索引',
    field: 'subLotIndex',
    width: 100,
  },
  { title: '发现人', field: 'reporter', editRender: { name: 'AInput' }, width: 120 },
  { title: '发现工序', field: 'reportProc', editRender: { name: 'AInput' }, width: 120 },
  { title: '异常原因', field: 'defectiveReason', editRender: { name: 'AInput' }, width: 150 },
  {
    title: '不良原因描述',
    field: 'defectiveDesc',
    editRender: { name: 'AInput' },
    width: 150,
  },
  {
    title: '责任单位',
    field: 'responseUnit',
    editRender: { name: 'AInput' },
    width: 150,
  },
  {
    title: '确认数量',
    field: 'executeQuantity',
    editRender: {
      name: 'AInputNumber',
    },
    width: 120,
    fixed: 'right',
  },
];

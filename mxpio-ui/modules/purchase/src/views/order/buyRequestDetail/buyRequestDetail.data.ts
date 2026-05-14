import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
    fixed: 'left',
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
    fixed: 'left',
  },
  { title: '供应商编码', field: 'pnCode', width: 100, fixed: 'left' },
  { title: '供应商名称', field: 'pnName', width: 100, fixed: 'left' },
  { title: '请购单号', field: 'bizNo', width: 120, fixed: 'left' },
  { title: '行号', field: 'lineNo', width: 60, fixed: 'left' },
  {
    title: '请购类型',
    field: 'buyRequestOrder.textMap.bizType$DICT_TEXT_',
    formatter: 'dictText',
    width: 120,
  },
  { title: '行状态', field: 'lineStatus', width: 100, formatter: 'dictText' },
  { title: '驳回原因', field: 'reason', width: 100 },
  { title: '计划跟踪号', field: 'trackingNo', width: 100 },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '物料组', field: 'itemGroupCode', width: 100, formatter: 'dictText' },
  { title: '单位', field: 'unitCode', width: 100, formatter: 'dictText' },
  { title: '请购数量', field: 'quantity', width: 100 },
  { title: '建议下单日期', field: 'suggestOrderDate', width: 120 },
  { title: '需求到货日期', field: 'arriveDate', width: 120 },
  { title: '用途', field: 'usage', width: 100 },
  { title: '申请人', field: 'buyRequestOrder.textMap.applyMan$DICT_TEXT_', width: 100 },
  { title: '申请部门', field: 'buyRequestOrder.textMap.applyDept$DICT_TEXT_', width: 100 },
  { title: '申请日期', field: 'buyRequestOrder.orderDate', width: 100 },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'bizNo',
    title: '请购单号',
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
    field: 'itemGroupCode@EQ',
    title: '物料组',
    itemRender: {
      name: 'ItemGroupSelect',
    },
    span: 6,
  },
  {
    field: 'lineStatus@EQ',
    title: '行状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_BUY_REQ_LINE_STATUS',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'pnCode',
    title: '供应商编码',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'pnName',
    title: '供应商名称',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'bpmnStatus@EQ',
    title: '审核状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'arriveDate',
    title: '需求到货日期',
    itemRender: {
      name: 'ADatePicker',
      props: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
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

export const rejectFormSchema: FormSchema[] = [
  {
    field: 'auditOpinion',
    label: '审核意见',
    required: true,
    component: 'Select',
    componentProps: () => {
      return {
        options: [
          { label: '同意', value: '01' },
          { label: '不同意', value: '02' },
          { label: '驳回', value: '03' },
        ],
        disabled: true,
      };
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'reason',
    label: '驳回原因',
    component: 'Input',
    required: true,
    colProps: {
      span: 24,
    },
  },
];

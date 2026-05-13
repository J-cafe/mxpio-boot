import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: '50', align: 'center', fixed: 'left' },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '不合格数量', field: 'unqualifiedQuantity', width: 100 },
  { title: '审核状态', field: 'bpmnStatus', width: 100, formatter: 'dictText' },
  { title: '使用决策', field: 'applyDecision', width: 100, formatter: 'dictText' },
  { title: '不良品处理单号', field: 'bizNo', width: 120 },
  { title: '行号', field: 'lineNo', width: 60 },
  {
    title: '质检单号',
    field: 'inspectionBillNo',
    width: 120,
    slots: { default: 'inspectionBillNo' },
  },
  { title: '业务类型', field: 'busiType', width: 100, formatter: 'dictText' },
  { title: '源单类型', field: 'originBizType', width: 100, formatter: 'dictText' },
  { title: '源单单号', field: 'originBizNo', width: 100 },
  { title: '报检组织', field: 'applyOrgName', width: 100 },
  { title: '质检员', field: 'inspector', width: 100, formatter: 'dictText' },
  { title: '质检时间', field: 'inspectionFinishTime', width: 120 },
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
    field: 'applyDecision@IN',
    title: '使用决策',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_SUBLOT_USAGE_DECISION',
        mode: 'multiple',
      },
    },
    span: 6,
  },
  {
    field: 'bizNo',
    title: '不良品处理单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },

  {
    field: 'inspectionBillNo',
    title: '质检单号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'originBizNo',
    title: '源单编号',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'applyOrgName',
    title: '报检组织',
    folding: true,
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'inspector@IN',
    title: '质检员',
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

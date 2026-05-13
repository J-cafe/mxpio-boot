import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import { dateUtil } from '@mxpio/utils';
import Big from 'big.js';

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
  { title: '质检单号', field: 'bizNo', width: 120 },
  { title: '业务类型', field: 'busiType', width: 120, formatter: 'dictText' },
  { title: '检验状态', field: 'checkStatus', formatter: 'dictText', width: 100 },
  { title: '源单类型', field: 'originBizType', formatter: 'dictText', width: 100 },
  { title: '源单编号', field: 'originBizNo', width: 100 },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '报检组织编号', field: 'applyOrgCode', width: 120 },
  { title: '报检组织名称', field: 'applyOrgName', width: 120 },
  { title: '检验结论', field: 'testResult', width: 100, formatter: 'dictText' },
  { title: '报检数量', field: 'quantity', width: 100 },
  { title: '检验数量', field: 'checkQuantity', width: 100 },
  { title: '不合格数量', field: 'unqualifiedQuantity', width: 100 },
  { title: '让步接收数量', field: 'concessionQuantity', width: 120 },
  { title: '判退数量', field: 'refundQuantity', width: 100 },
  { title: '合格数量', field: 'receiveQuantity', width: 100 },
  {
    title: '接收数量',
    field: 'receiveQuantity_',
    width: 100,
    slots: {
      default: ({ row }) => {
        if (row.receiveQuantity && row.concessionQuantity) {
          return (Number(row.receiveQuantity) + Number(row.concessionQuantity)).toFixed(2);
        }
        return '';
      },
    },
  },
  { title: '不合格类型', field: 'unqualifiedType', formatter: 'dictText', width: 120 },
  { title: '不合格描述', field: 'unqualifiedDesc', width: 120 },
  { title: '创建时间', field: 'createTime', width: 120 },
  { title: '检验员', field: 'inspector', formatter: 'dictText', width: 100 },
  { title: '检验开始时间', field: 'inspectionStartTime', width: 120 },
  { title: '检验完成时间', field: 'inspectionFinishTime', width: 120 },
  { title: '备注', field: 'memo', width: 120 },
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
    title: '质检单编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'busiType',
    title: '业务类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_IS_BUSI_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'checkStatus',
    title: '检验状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_CHECK_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'applyOrgCode@EQ',
    title: '报检组织编号',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'applyOrgName@EQ',
    title: '报检组织名称',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'originBizType',
    title: '源单类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_QUAL_ORIGIN_BIZ_TYPE',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'originBizNo',
    title: '源单编号',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
    span: 6,
  },
  {
    field: 'itemCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
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
    field: 'inspector@EQ',
    title: '检验员',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
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
    field: 'inspectionFinishTime',
    title: '检验完成时间',
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
    label: '质检单编号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'busiType',
    label: '业务类型',
    component: 'DictSelect',
    componentProps: {
      disabled: true,
      dictCode: 'ERP_QUAL_IS_BUSI_TYPE',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'checkStatus',
    label: '检验状态',
    component: 'DictSelect',
    componentProps: {
      disabled: true,
      dictCode: 'ERP_QUAL_CHECK_STATUS',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'applyOrgCode',
    label: '报检组织编号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'applyOrgName',
    label: '报检组织名称',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'originBizType',
    label: '源单类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_ORIGIN_BIZ_TYPE',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'originBizNo',
    label: '源单编号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemCode',
    label: '物料编码',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemName',
    label: '物料名称',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemSpec',
    label: '规格型号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'drawingNo',
    label: '图号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'unitCode',
    label: '单位',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_TECH_UNIT_CODE',
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'quantity',
    label: '报检数量',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'checkQuantity',
    label: '检验数量',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'testResult',
    label: '批次检验结论',
    component: 'DictSelect',
    required: true,
    componentProps: ({ formModel }) => {
      return {
        dictCode: 'MB_QUALITY_CHECK_RESULT',
        onChange: (value) => {
          if (value === '1') {
            formModel.refundQuantity = 0;
            formModel.concessionQuantity = 0;
            formModel.unqualifiedQuantity = 0;
            formModel.receiveQuantity = Number(formModel.quantity).toFixed(2);
          }
        },
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'unqualifiedQuantity',
    label: '不合格数量',
    required: true,
    component: 'InputNumber',
    componentProps: ({ formModel }) => {
      return {
        onChange: (value) => {
          formModel.refundQuantity = 0;
          formModel.concessionQuantity = 0;
          formModel.receiveQuantity = Big(formModel.quantity).minus(value).toFixed(2);
        },
      };
    },
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (Number(value) < 0) {
              return Promise.reject('不能小于0!');
            }
            if (Number(value) > Number(values.quantity)) {
              return Promise.reject('不能大于报检数量!');
            }
            return Promise.resolve();
          },
        },
      ];
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'concessionQuantity',
    label: '让步接收数量',
    component: 'InputNumber',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'refundQuantity',
    label: '判退数量',
    component: 'InputNumber',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'receiveQuantity',
    label: '合格数量',
    component: 'InputNumber',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'receiveQuantity',
    label: '接收数量',
    // component: 'InputNumber',
    slot: 'receive',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inspector',
    label: '检验员',
    component: 'UserByDeptSelect',
    required: true,
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inspectionStartTime',
    label: '检验开始时间',
    // required: true,
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      showTime: true,
    },
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('开始时间不能为空');
            }
            if (
              value &&
              values.inspectionFinishTime &&
              dateUtil(value).isAfter(values.inspectionFinishTime)
            ) {
              return Promise.reject('开始时间必须早于完成时间!');
            }
            return Promise.resolve();
          },
        },
      ];
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inspectionFinishTime',
    label: '检验完成时间',
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      showTime: true,
    },
    dynamicRules: ({ values }) => {
      return [
        {
          validator: (_, value) => {
            if (
              value &&
              values.inspectionStartTime &&
              dateUtil(value).isBefore(values.inspectionStartTime)
            ) {
              return Promise.reject(' 完成时间必须晚于开始时间!');
            }
            return Promise.resolve();
          },
        },
      ];
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'unqualifiedType',
    label: '不合格类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_UNQUALIFIED_TYPE',
      mode: 'multiple',
      arrayToString: true,
    },
    ifShow: (formData) => {
      return formData.values?.unqualifiedQuantity > 0;
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'unqualifiedDesc',
    label: '不合格描述',
    component: 'InputTextArea',
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

export const sampleFormSchema: FormSchema[] = [
  {
    field: 'bizNo',
    label: '质检单编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'busiType',
    label: '业务类型',
    component: 'DictSelect',
    componentProps: {
      disabled: true,
      dictCode: 'ERP_QUAL_IS_BUSI_TYPE',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'checkStatus',
    label: '检验状态',
    component: 'DictSelect',
    componentProps: {
      disabled: true,
      dictCode: 'ERP_QUAL_CHECK_STATUS',
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'applyOrgCode',
    label: '报检组织编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'applyOrgName',
    label: '报检组织名称',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'originBizType',
    label: '源单类型',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_QUAL_ORIGIN_BIZ_TYPE',
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'originBizNo',
    label: '源单编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemCode',
    label: '物料编码',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemName',
    label: '物料名称',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'itemSpec',
    label: '规格型号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'drawingNo',
    label: '图号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'unitCode',
    label: '单位',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_TECH_UNIT_CODE',
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'quantity',
    label: '报检数量',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'checkQuantity',
    label: '检验数量',
    component: 'InputNumber',
    colProps: {
      span: 8,
    },
    required: true,
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (Number(value) <= 0) {
              return Promise.reject('检验数量不能小于0!');
            }
            if (Number(value) > Number(values.quantity)) {
              return Promise.reject('检验数量不能大于报检数量!');
            }
            return Promise.resolve();
          },
        },
      ];
    },
  },
];

export const bySampleColumns: VxeGridPropTypes.Columns = [
  { title: '检验顺序', field: 'sort', width: 100, sortable: true },
  { title: '项目来源', field: 'detectSource', width: 100, formatter: 'dictText' },
  { title: '检测项目编号', field: 'inspectionItemCode', width: 140 },
  { title: '检测项目名称', field: 'inspectionItemName', width: 120 },
  {
    title: '实测值',
    field: 'measurement',
    editRender: {},
    slots: { edit: 'measurement', default: 'measurementText' },
    width: 200,
  },
  { title: '检验结论', field: 'testResult', formatter: 'dictText', width: 100 },
  { title: '项目类型', field: 'inspectionItemType', width: 100, formatter: 'dictText' },
  { title: '项目分类', field: 'inspectionItemClass', width: 100, formatter: 'dictText' },
  { title: '缺陷等级', field: 'inspectionDefectGrade', width: 100, formatter: 'dictText' },
  { title: '检验标准', field: 'inspectionDtandard', width: 100 },
  { title: '检测工具', field: 'inspectionDetectionTool', width: 100 },
  { title: '检验方法', field: 'inspectionDetectionMethod', width: 100 },
  {
    title: '比较符',
    field: 'comparator',
    width: 120,
    slots: {
      default: ({ row }) => {
        return row.comparator === 'include' ? '[]' : row.comparator;
      },
    },
  },
  { title: '目标值', field: 'targetValue', width: 120 },
  { title: '单位', field: 'units', width: 120 },
  { title: '最大值', field: 'maxValue', width: 120 },
  { title: '最小值', field: 'minValue', width: 120 },
  { title: '备注', field: 'memo', editRender: { name: 'AInput' }, width: 120 },
];

export const byDetectColumns: VxeGridPropTypes.Columns = [
  { type: 'seq', width: 40 },
  { title: '样本编码', field: 'simpleCode', width: 100 },
  { title: '项目来源', field: 'detectSource', width: 100, formatter: 'dictText' },
  { title: '检测项目编号', field: 'inspectionItemCode', width: 140 },
  { title: '检测项目名称', field: 'inspectionItemName', width: 120 },
  {
    title: '实测值',
    field: 'measurement',
    editRender: {},
    slots: { edit: 'measurement', default: 'measurementText' },
    width: 200,
  },
  { title: '检验结论', field: 'testResult', formatter: 'dictText', width: 100 },
  { title: '项目类型', field: 'inspectionItemType', width: 100, formatter: 'dictText' },
  { title: '项目分类', field: 'inspectionItemClass', width: 100, formatter: 'dictText' },
  { title: '缺陷等级', field: 'inspectionDefectGrade', width: 100, formatter: 'dictText' },
  { title: '检验标准', field: 'inspectionDtandard', width: 100 },
  { title: '检测工具', field: 'inspectionDetectionTool', width: 100 },
  { title: '检验方法', field: 'inspectionDetectionMethod', width: 100 },
  {
    title: '比较符',
    field: 'comparator',
    width: 120,
    slots: {
      default: ({ row }) => {
        return row.comparator === 'include' ? '[]' : row.comparator;
      },
    },
  },
  { title: '目标值', field: 'targetValue', width: 120 },
  { title: '单位', field: 'units', width: 120, formatter: 'dictText' },
  { title: '最大值', field: 'maxValue', width: 120 },
  { title: '最小值', field: 'minValue', width: 120 },
  { title: '备注', field: 'memo', editRender: { name: 'AInput' }, width: 120 },
];

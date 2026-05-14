import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import { positiveNumberPattern } from '@mxpio/bizcommon';
import { dateUtil, isNaN } from '@mxpio/utils';

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
  { title: '单位', field: 'unitCode', width: 100, formatter: 'dictText' },
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

export const formSchema: FormSchema[] = [
  {
    field: 'bizNo',
    label: '委外订单号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'orderDate',
    label: '单据日期',
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderType',
    label: '单据类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_MO_ORDER_TYPE',
        disabled: true,
      };
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
    field: 'pnCode',
    label: '加工商',
    component: 'SupplySelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (itemCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            pnName: rows?.pnName || '',
            bizMan: rows?.bizMan || '',
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'pnName',
    label: '加工商名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'bizMan',
    label: '业务员',
    component: 'UserByDeptSelect',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'productItemCode',
    label: '产品编码',
    component: 'MaterialSelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        filters: {
          'itemSource@EQ': '1',
        },
        onSelect: (itemCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            productItemName: rows?.itemName || '',
            itemSpec: rows?.itemSpec || '',
            unitCode: rows?.unitCode || '',
            productDrawingNo: rows?.drawingNo || '',
            productItemCode: itemCode,
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 6,
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
      span: 6,
    },
  },
  {
    field: 'itemSpec',
    label: '规格型号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
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
    field: 'productDrawingNo',
    label: '图号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'orderQuantity',
    label: '订单数量',
    component: 'InputNumber',
    componentProps: ({ formActionType }) => {
      return {
        onChange: (value) => {
          const { setFieldsValue, getFieldsValue } = formActionType;
          const { orderQuantity } = getFieldsValue();
          if (isNaN(value) || isNaN(orderQuantity)) {
            return false;
          }
          setFieldsValue({
            totalAmount: (value * orderQuantity).toFixed(2),
          });
        },
      };
    },
    required: true,
    rules: [
      { pattern: positiveNumberPattern as RegExp, message: '请输入大于0的数字', trigger: 'blur' },
    ],
    colProps: {
      span: 6,
    },
  },
  {
    field: 'taxPrice',
    label: '单价',
    component: 'InputNumber',
    componentProps: ({ formActionType }) => {
      return {
        onChange: (value) => {
          const { setFieldsValue, getFieldsValue } = formActionType;
          const { orderQuantity } = getFieldsValue();
          if (isNaN(value) || isNaN(orderQuantity)) {
            return false;
          }
          setFieldsValue({
            totalAmount: (value * orderQuantity).toFixed(2),
          });
        },
      };
    },
    required: true,
    rules: [
      { pattern: positiveNumberPattern as RegExp, message: '请输入大于0的数字', trigger: 'blur' },
    ],
    colProps: {
      span: 6,
    },
  },
  {
    field: 'totalAmount',
    label: '金额',
    component: 'InputNumber',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'startDate',
    label: '计划开工日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      class: '!w-full',
    },
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('计划开工日期不能为空');
            }
            if (value && values.endDate && dateUtil(value).isAfter(values.endDate)) {
              return Promise.reject('完工日期需要晚于开工日期!');
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
    field: 'endDate',
    label: '计划完工日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      class: '!w-full',
    },
    dynamicRules: ({ values }) => {
      return [
        {
          required: true,
          validator: (_, value) => {
            if (!value) {
              return Promise.reject('计划完工日期不能为空');
            }
            if (value && values.startDate && dateUtil(value).isBefore(values.startDate)) {
              return Promise.reject('完工日期需要晚于开工日期!');
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
    field: 'referenceDate',
    label: '参考日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      class: '!w-full',
      showTime: {
        defaultValue: [
          dateUtil('00:00:00', 'HH:mm:ss'), // 开始时间：00:00:00
          dateUtil('23:59:59', 'HH:mm:ss'), // 结束时间：23:59:59
        ],
      },
    },
    required: true,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'entrustDept',
    label: '委外单位',
    component: 'DeptSelect',
    colProps: {
      span: 6,
    },
  },
  {
    field: 'memo',
    label: '加工要求',
    component: 'InputTextArea',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'machiningDrawing',
    label: '加工图纸',
    component: 'Upload',
    colProps: {
      span: 24,
    },
  },
];

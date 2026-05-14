import type { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import { eqpCategoryTreeApi } from '@mxpio/bizcommon';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'radio',
    width: 40,
  },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '设备编码', field: 'eqpCode', sortable: true },
  { title: '设备名称', field: 'eqpName', sortable: true },
  { title: '规格型号（简）', field: 'specType', sortable: true },
  { title: '规格型号（全）', field: 'specTypeFull', sortable: true },
  { title: '设备分类', field: 'eqpTypeCode', formatter: 'dictText' },
  { title: '是否关键设备', field: 'isKey', formatter: 'dictText' },
  { title: '是否附属设备', field: 'isSubOrdinate', formatter: 'dictText' },
  { title: '是否检定', field: 'isCalibrate', formatter: 'dictText' },
  { title: '检定周期', field: 'calibrateCycle' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'eqpName',
    title: '设备名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'eqpCode',
    title: '设备编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'eqpTypeCode',
    title: '设备分类',
    itemRender: {
      name: 'AApiTreeSelect',
      props: {
        api: eqpCategoryTreeApi,
        labelField: 'name',
        valueField: 'code',
        resultField: 'list',
        class: '!w-full',
      },
    },
    span: 6,
  },
  {
    field: 'specType',
    title: '规格型号',
    itemRender: {
      name: 'AInput',
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

export const attrColumns: VxeGridPropTypes.Columns = [
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
  { title: '档案名称', field: 'attrName' },
  { title: '档案类型', field: 'attrType', formatter: 'dictText' },
  { title: '附件', field: 'attrUrl', slots: { default: 'attrUrl' } },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建日期', field: 'createTime' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const attrFormSchema: FormSchema[] = [
  {
    field: 'attrName',
    label: '档案名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'attrType',
    label: '档案类型',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_EQUIPMENT_ATTR_TYPE',
      numberToString: true,
    },
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'attrUrl',
    label: '附件',
    component: 'Upload',
    colProps: {
      span: 24,
    },
  },
];

export const bomColumns: VxeGridPropTypes.Columns = [
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
  { title: 'BOM名称', field: 'bomName' },
  { title: 'BOM编码', field: 'bomCode' },
  { title: '规格型号', field: 'specType' },
  { title: '生命周期(月)', field: 'lifeCycle' },
  { title: '需求数量', field: 'quantity' },
  { title: '图片', field: 'pic', slots: { default: 'pic' } },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const bomFormSchema: FormSchema[] = [
  {
    field: 'bomCode',
    label: 'BOM编码',
    component: 'MaterialSelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (pnCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            bomName: rows?.itemName || '',
            specType: rows?.itemSpec || '',
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'bomName',
    label: 'BOM名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'specType',
    label: '规格型号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'lifeCycle',
    label: '生命周期(月)',
    component: 'Input',
    componentProps: {
      type: 'number',
      suffix: '月',
    },
    required: true,
    rules: [
      {
        type: 'number',
        min: 0,
        message: '请输入大于0的数字',
        transform: (value: string) => Number(value),
        trigger: 'blur',
      },
    ],
    colProps: {
      span: 24,
    },
  },
  {
    field: 'quantity',
    label: '需求数量',
    component: 'Input',
    componentProps: {
      type: 'number',
    },
    rules: [
      {
        type: 'number',
        min: 0,
        message: '请输入大于0的数字',
        transform: (value: string) => Number(value),
        trigger: 'blur',
      },
    ],
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'pic',
    label: '图片',
    component: 'Upload',
    componentProps: {
      accept: ['image/*'],
    },
    colProps: {
      span: 24,
    },
  },
];

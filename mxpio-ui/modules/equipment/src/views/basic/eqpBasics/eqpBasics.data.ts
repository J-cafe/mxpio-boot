import type { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

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
  { title: '资产编码', field: 'basicsCode', width: 100, sortable: true },
  { title: '资产名称', field: 'eqpName', width: 100, sortable: true },
  { title: '设备编码', field: 'eqpCode', width: 100, sortable: true },
  { title: '设备状态', field: 'status', formatter: 'dictText', width: 100 },
  { title: '父级设备', field: 'parentCode', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '规格型号（简）', field: 'specType', width: 100 },
  { title: '规格型号（全）', field: 'specTypeFull', width: 100 },
  { title: '制造商', field: 'manufacturer', width: 100, formatter: 'dictText' },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '出厂日期', field: 'productDate', width: 100 },
  { title: '启用时间', field: 'checkDate', width: 100 },
  { title: '质保期', field: 'installDate', width: 100 },
  { title: '是否关键设备', field: 'isKey', formatter: 'dictText', width: 120 },
  { title: '是否附属设备', field: 'isSubOrdinate', formatter: 'dictText', width: 120 },
  { title: '是否送检', field: 'isCalibrate', formatter: 'dictText', width: 100 },
  { title: '送检周期（天）', field: 'calibrateCycle', formatter: 'dictText', width: 120 },
  { title: '设备序列号', field: 'seqNumber', width: 100 },
  { title: '供应商编号', field: 'pnCode', width: 100 },
  { title: '供应商名称', field: 'pnName', width: 120 },
  { title: '备注', field: 'remarks', width: 100 },
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
    field: 'basicsCode',
    title: '资产编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'eqpName',
    title: '资产名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'status@IN',
    title: '设备状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_EQP_STATUS',
        mode: 'multiple',
      },
    },
    span: 6,
  },
  {
    field: 'eqpCode',
    title: '设备编码',
    itemRender: {
      name: 'AInput',
    },
    folding: true,
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
    field: 'codeAbc@IN',
    title: '设备等级',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_EQP_CODE_ABC',
        mode: 'multiple',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'isSubOrdinate@EQ',
    title: '是否附属设备',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_YESNO',
      },
    },
    folding: true,
    span: 6,
  },
  {
    field: 'useDeptId@EQ',
    title: '使用单位',
    itemRender: {
      name: 'DeptSelect',
      props: {
        multiple: false,
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

export const sopColumns: VxeGridPropTypes.Columns = [
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
  { title: 'SOP名称', field: 'attrName' },
  { title: 'SOP类型', field: 'sopType', formatter: 'dictText' },
  { title: '附件', field: 'attrUrl', slots: { default: 'downloadAttr' } },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const sopFormSchema: FormSchema[] = [
  {
    field: 'attrName',
    label: 'SOP名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'sopType',
    label: 'SOP类型',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_EQUIPMENT_GROUP_CATEGORY',
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

export const remarkColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  { title: '备注内容', field: 'remark' },
  { title: '填写人', field: 'fillPeople', formatter: 'dictText' },
  { title: '填写日期', field: 'fillDate' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const remarkFormSchema: FormSchema[] = [
  {
    field: 'remark',
    label: '备注内容',
    component: 'InputTextArea',
    required: true,
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillPeople',
    label: '填报人',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'fillDate',
    label: '填报时间',
    component: 'DatePicker',
    colProps: {
      span: 24,
    },
    componentProps: {
      class: '!w-full',
    },
  },
];

export const subColumns: VxeGridPropTypes.Columns = [
  { title: '资产编码', field: 'basicsCode', width: 180, treeNode: true, fixed: 'left' },
  { title: '资产名称', field: 'eqpName', width: 100 },
  { title: '设备编码', field: 'eqpCode', width: 100 },
  { title: '设备状态', field: 'status', formatter: 'dictText', width: 100 },
  { title: '设备分类', field: 'eqpTypeId', formatter: 'dictText', width: 100 },
  { title: '规格型号', field: 'specType', width: 100 },
  { title: '制造商', field: 'manufacturer', width: 100 },
  { title: '设备等级', field: 'codeAbc', formatter: 'dictText', width: 100 },
  { title: '使用单位', field: 'useDeptId', formatter: 'dictText', width: 100 },
  { title: '出厂日期', field: 'productDate', width: 100 },
  { title: '启用时间', field: 'checkDate', width: 100 },
  { title: '质保期', field: 'installDate', width: 100 },
  { title: '是否关键设备', field: 'isKey', formatter: 'dictText', width: 120 },
  { title: '是否附属设备', field: 'isSubOrdinate', formatter: 'dictText', width: 120 },
  { title: '是否送检', field: 'isCalibrate', formatter: 'dictText', width: 100 },
  { title: '送检周期（天）', field: 'calibrateCycle', formatter: 'dictText', width: 120 },
  { title: '设备序列号', field: 'seqNumber', width: 100 },
  { title: '供应商编号', field: 'pnCode', width: 100 },
  { title: '供应商名称', field: 'pnName', width: 120 },
];

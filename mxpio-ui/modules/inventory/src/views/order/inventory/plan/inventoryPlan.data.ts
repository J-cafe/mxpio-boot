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
  { title: '计划编码', field: 'code', width: 150 },
  { title: '计划名称', field: 'name', width: 100 },
  { title: '计划状态', field: 'execStatus', formatter: 'dictText', width: 100 },
  { title: '计划类型', field: 'planType', formatter: 'dictText', width: 100 },
  { title: '开始日期', field: 'startDate', width: 120 },
  { title: '频率', field: 'rate', width: 80 },
  { title: '频率单位', field: 'rateUnit', formatter: 'dictText', width: 80 },
  { title: '结束类型', field: 'endLoopType', width: 80, formatter: 'dictText' },
  {
    title: '结束次数/日期',
    field: 'endTimes',
    width: 120,
    slots: {
      default: ({ row }) => {
        let text = '';
        if (row.endLoopType === 'frequency') {
          text = row.endTimes;
        } else if (row.endLoopType === 'timing') {
          text = row.endDate;
        }
        return text;
      },
    },
  },
  { title: '生效状态', field: 'isEnable', formatter: 'dictText', width: 80 },
  { title: '盘点类型', field: 'inventoryType', formatter: 'dictText', width: 80 },
  { title: '盘点方法', field: 'inventoryWay', formatter: 'dictText', width: 80 },
  { title: '盘点范围', field: 'inventoryRange', formatter: 'dictText', width: 100 },
  { title: '盘点比例', field: 'inventoryPercentage', width: 80 },
  { title: '比例依据', field: 'inventoryRangeBasis', formatter: 'dictText', width: 100 },
  { title: '仓库名称', field: 'whCode', formatter: 'dictText', width: 100 },
  { title: '盘点人员', field: 'inventoryPersons', formatter: 'dictText', width: 100 },
  { title: '备注', field: 'memo', width: 100 },
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
    field: 'name',
    title: '计划名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'code',
    title: '计划编码',
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

export const formSchema: FormSchema[] = [
  {
    field: '',
    label: '盘点计划',
    component: 'Divider',
    colProps: {
      span: 24,
    },
    componentProps: {
      orientation: 'left',
    },
  },
  {
    field: 'code',
    label: '计划编码',
    component: 'Input',
    componentProps: () => {
      return {
        disabled: true,
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'name',
    label: '计划名称',
    required: true,
    component: 'Input',
    colProps: {
      span: 6,
    },
  },
  {
    field: 'planType',
    label: '计划类型',
    required: true,
    component: 'DictSelect',
    componentProps: ({ formModel }) => {
      return {
        dictCode: 'ERP_INV_PLAN_TYPE',
        onChange: (value) => {
          if (value === '2') {
            formModel.endLoopType = '';
            formModel.endTimes = '';
            formModel.endDate = '';
          }
        },
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'startDate',
    label: '开始日期',
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
    field: 'rate',
    label: '频率',
    required: true,
    component: 'Input',
    colProps: {
      span: 6,
    },
  },
  {
    field: 'rateUnit',
    label: '频率单位',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_PLAN_RATE_UNIT',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'endLoopType',
    label: '结束类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_PLAN_END_LOOP_TYPE',
    },
    colProps: {
      span: 6,
    },
    ifShow: (formData) => {
      return formData.values?.planType !== '2';
    },
  },
  {
    field: 'endTimes',
    label: '结束次数',
    required: true,
    component: 'InputNumber',
    componentProps: {
      class: '!w-full',
    },
    colProps: {
      span: 6,
    },
    ifShow: (formData) => {
      return formData.values?.endLoopType === 'frequency' && formData.values?.planType !== '2';
    },
  },
  {
    field: 'endDate',
    label: '结束日期',
    required: true,
    component: 'DatePicker',
    componentProps: {
      class: '!w-full',
    },
    colProps: {
      span: 6,
    },
    ifShow: (formData) => {
      return formData.values?.endLoopType === 'timing' && formData.values?.planType !== '2';
    },
  },
  {
    field: 'cyclePercentage',
    label: '每次循环比例',
    required: true,
    component: 'InputNumber',
    componentProps: {
      class: '!w-full',
    },
    colProps: {
      span: 6,
    },
    ifShow: (formData) => {
      return formData.values?.planType === '2';
    },
  },
  {
    field: 'orderStatus',
    label: '生效状态',
    required: true,
    component: 'RadioButtonGroup',
    componentProps: () => {
      return {
        options: [
          {
            label: '是',
            value: '1',
          },
          {
            label: '否',
            value: '0',
          },
        ],
      };
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
  {
    field: '',
    label: '盘点配置',
    component: 'Divider',
    colProps: {
      span: 24,
    },
    componentProps: {
      orientation: 'left',
    },
  },
  {
    field: 'inventoryType',
    label: '盘点类型',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_INVENTORY_TYPE',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inventoryWay',
    label: '盘点方法',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_INVENTORY_WAY',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inventoryRange',
    label: '盘点范围',
    required: true,
    component: 'DictSelect',
    componentProps: ({ formModel }) => {
      return {
        dictCode: 'ERP_INV_INVENTORY_RANGE',
        onChange: (value) => {
          if (value === '0') {
            formModel.inventoryPercentage = 100;
          }
        },
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inventoryPercentage',
    label: '盘点比例',
    required: true,
    component: 'InputNumber',
    componentProps: ({ formModel }) => {
      return {
        class: '!w-full',
        disabled: formModel.inventoryRange === '0',
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inventoryRangeBasis',
    label: '比例依据',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_INVENTORY_RANGE_BASIS',
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'whCode',
    label: '盘点仓库',
    required: true,
    component: 'WareHouseSelect',
    colProps: {
      span: 6,
    },
  },
  {
    field: 'inventoryPersons',
    label: '盘点人员',
    required: true,
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'isExcludeZero',
    label: '库存为0不参与盘点',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        {
          label: '参与',
          value: '1',
        },
        {
          label: '不参与',
          value: '0',
        },
      ],
    },
    labelWidth: 140,
    colProps: {
      span: 6,
    },
  },
  {
    field: 'isAppend',
    label: '盘点中允许增加物料',
    component: 'RadioButtonGroup',
    componentProps: {
      options: [
        {
          label: '允许',
          value: '1',
        },
        {
          label: '不允许',
          value: '0',
        },
      ],
    },
    labelWidth: 140,
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
  {
    title: '数量',
    field: 'quantity',
    width: 80,
  },
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
    },
    width: 120,
  },
  {
    title: '批次号',
    field: 'lotNo',
    width: 120,
  },
];

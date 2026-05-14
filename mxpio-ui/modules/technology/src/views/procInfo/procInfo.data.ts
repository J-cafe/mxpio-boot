import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40, fixed: 'left' },
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  { title: '工序编码', field: 'processCode' },
  { title: '工序名称', field: 'processName' },
  { title: '工序分类', field: 'processClass', formatter: 'dictText' },
  { title: '工序类型', field: 'processType', formatter: 'dictText' },
  { title: '所属车间', field: 'wkshopCode', formatter: 'dictText' },
  { title: '报工方式', field: 'reportMode', formatter: 'dictText' },
  { title: '是否自动报工', field: 'autoReportAble', formatter: 'dictText' },
  { title: '允差范围', field: 'toleranceRange', formatter: 'dictText' },
  { title: '是否委外', field: 'outsourceAble', formatter: 'dictText' },
  { title: '是否启用', field: 'status', formatter: 'dictText' },
  { title: '工艺描述', field: 'craftDesc' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'processCode',
    title: '工序编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'processName',
    title: '工序名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'processType@EQ',
    title: '工序类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_PROC_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'processClass@EQ',
    title: '工序分类',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_PROC_CLASS',
      },
    },
    span: 6,
  },
  {
    field: 'wkshopCode@EQ',
    title: '所属车间',
    folding: true,
    itemRender: {
      name: 'WorkShopSelect',
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
    field: 'processCode',
    label: '工序编码',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: () => {
      return {
        disabled: true,
      };
    },
  },
  {
    field: 'processName',
    label: '工序名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'processType',
    label: '工序分类',
    component: 'DictSelect',
    required: true,
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_PROC_TYPE',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'processClass',
    label: '工序类型',
    component: 'DictSelect',
    required: true,
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_PROC_CLASS',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'wkshopCode',
    label: '所属车间',
    required: true,
    component: 'WorkShopSelect',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'reportMode',
    label: '报工方式',
    component: 'DictSelect',
    required: true,
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_REPORT_MODE',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'manufactureAble',
    label: '是否自制',
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
      span: 12,
    },
  },
  {
    field: 'outsourceAble',
    label: '允许委外',
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
      span: 12,
    },
  },
  {
    field: 'batchReportAble',
    label: '分批报工',
    component: 'RadioButtonGroup',
    componentProps: ({ formActionType }) => {
      return {
        onChange: (value) => {
          const { setFieldsValue } = formActionType;
          if (value !== '1') {
            setFieldsValue({
              toleranceRange: '',
              autoReportAble: '0',
            });
          } else {
            setFieldsValue({
              autoReportAble: '1',
            });
          }
        },
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
      span: 12,
    },
  },
  {
    field: 'toleranceRange',
    label: '允差范围',
    component: 'InputNumber',
    required: true,
    colProps: {
      span: 12,
    },
    rules: [
      { required: true, message: '请输入允差范围' },
      {
        type: 'number',
        min: 0,
        max: 100,
        message: '允差范围必须在0-100之间',
        transform: (value) => {
          if (value === '' || value === undefined || value === null) {
            return 0;
          } else {
            return Number(value);
          }
        },
      },
    ],
    ifShow: (formData) => {
      return formData.values?.batchReportAble === '1';
    },
  },
  {
    field: 'autoReportAble',
    label: '自动报工',
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
      span: 12,
    },
    ifShow: (formData) => {
      return formData.values?.batchReportAble === '1';
    },
  },
  {
    field: 'finishCheckFlag',
    label: '完工检验',
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
      span: 12,
    },
  },

  {
    field: 'eqpCode',
    label: '设备编码',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'eqpName',
    label: '设备名称',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'docFile',
    label: '工艺文件',
    component: 'Upload',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'status',
    label: '是否启用',
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
      span: 12,
    },
  },
  {
    field: 'craftDesc',
    label: '工艺描述',
    component: 'InputTextArea',
    colProps: {
      span: 12,
    },
  },
];

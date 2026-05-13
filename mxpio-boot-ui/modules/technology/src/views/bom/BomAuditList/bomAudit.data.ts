import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '审核状态',
    field: 'useType',
    formatter: 'dictText',
  },
  {
    title: '父物料编码',
    field: 'parentCode',
    width: 120,
    sortable: true,
  },
  {
    title: '父物料名称',
    field: 'parentName',
  },
  {
    title: '父规格型号',
    field: 'parentSpec',
  },
  {
    title: '父物料单位',
    field: 'parentUnit',
    formatter: 'dictText',
  },
  {
    title: '父物料图号',
    field: 'parentDrawingNo',
  },
  {
    title: '物料组',
    field: 'parentGroup',
    formatter: 'dictText',
  },
  {
    title: '生效日期',
    field: 'beginTime',
  },
  {
    title: '变更单号',
    field: 'changeCode',
  },
  {
    title: '变更人',
    field: 'changeMan',
    formatter: 'dictText',
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
  },
  {
    title: '创建日期',
    field: 'createTime',
  },

  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'parentCode',
    title: '物料编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'parentName',
    title: '物料名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'parentSpec',
    title: '规格型号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'parentGroup@EQ',
    title: '物料组',
    itemRender: {
      name: 'ItemGroupSelect',
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
    field: 'changeCode',
    label: '变更单编号',
    component: 'Input',
    colProps: {
      span: 8,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'parentCode',
    label: '父项物料编码',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'parentName',
    label: '父项物料名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'parentSpec',
    label: '父项规格型号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'parentDrawingNo',
    label: '父项物料图号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'parentUnit',
    label: '父项物料单位',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_UNIT_CODE',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'parentGroup',
    label: '物料组',
    component: 'ItemGroupSelect',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'bomType',
    label: 'BOM类型',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_BOM_TYPE',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'useType',
    label: '使用状态',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_USE_TYPE',
        disabled: true,
      };
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'version',
    label: '版本号',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'beginTime',
    label: '生效日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD HH:mm:ss',
      class: 'w-full',
      disabled: true,
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeMan',
    label: '变更人',
    component: 'UserByDeptSelect',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeDate',
    label: '变更日期',
    component: 'DatePicker',
    componentProps: {
      valueFormat: 'YYYY-MM-DD',
      class: 'w-full',
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'changeMemo',
    label: '变更说明',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
];

export const formAuditSchema: FormSchema[] = [
  {
    field: 'agree',
    label: '是否同意',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'MB_SYSTEM_YES_NO',
      };
    },
    colProps: {
      span: 8,
    },
    required: true,
  },
  {
    field: 'auditOpinion',
    label: '审核意见',
    component: 'InputTextArea',
    colProps: {
      span: 8,
    },
  },
];

export const detailColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '子项物料编码',
    field: 'itemCode',
    width: 140,
  },
  {
    title: '子项物料名称',
    field: 'itemName',
    width: 120,
  },
  {
    title: '子项规格型号',
    field: 'itemSpec',
    width: 120,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 100,
  },
  {
    title: '子项物料组',
    field: 'itemProp',
    width: 100,
  },
  {
    title: '是否原材料',
    field: 'material',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '材料类型',
    field: 'materialType',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '材料牌号',
    field: 'materialBrand',
    width: 100,
  },
  {
    title: '基本用量',
    field: 'itemConsumption',
    width: 120,
  },
  {
    title: '下料长度',
    field: 'makeLength',
    width: 120,
  },
  {
    title: '下料宽度',
    field: 'makeArea',
    width: 120,
  },
  {
    title: '可制数量',
    field: 'makeNum',
    width: 120,
  },
  {
    title: '损耗率',
    field: 'wastage',
    width: 120,
  },
];

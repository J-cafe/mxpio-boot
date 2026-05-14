import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
import { Tag } from 'ant-design-vue';
import { h } from 'vue';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '盘点单号', field: 'inventoryCode', width: 100 },
  { title: '盘点类型', field: 'inventoryType', formatter: 'dictText', width: 80 },
  { title: '盘点方法', field: 'inventoryWay', formatter: 'dictText', width: 80 },
  { title: '盘点范围', field: 'inventoryRange', formatter: 'dictText', width: 80 },
  { title: '盘点比例', field: 'inventoryPercentage', width: 80 },
  { title: '比例依据', field: 'inventoryRangeBasis', formatter: 'dictText', width: 120 },
  { title: '账务日期', field: 'busiDate', width: 100 },
  { title: '单据状态', field: 'inventoryStatus', width: 80, formatter: 'dictText' },
  { title: '仓库名称', field: 'whCode', formatter: 'dictText', width: 100 },
  { title: '计划盘点日期', field: 'inventoryPlanDate', width: 120 },
  { title: '实际盘点日期', field: 'inventoryActualDate', width: 120 },
  { title: '盘点人员', field: 'inventoryPersons', formatter: 'dictText', width: 100 },
  { title: '计划编码', field: 'planCode', width: 100 },
  { title: '创建人', field: 'createBy', formatter: 'dictText', width: 100 },
  { title: '创建日期', field: 'createTime', width: 100 },
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
    field: 'inventoryCode',
    title: '盘点单号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'inventoryType@EQ',
    title: '盘点类型',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_INVENTORY_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'inventoryWay@EQ',
    title: '盘点方法',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_INVENTORY_WAY',
      },
    },
    span: 6,
  },
  {
    field: 'inventoryRange@EQ',
    title: '盘点范围',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_INVENTORY_RANGE',
      },
    },
    span: 6,
  },
  {
    field: 'whCode',
    title: '仓库',
    folding: true,
    itemRender: {
      name: 'WareHouseSelect',
    },
    span: 6,
  },
  {
    field: 'createTime',
    title: '创建日期',
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
    field: 'inventoryActualDate',
    title: '实际盘点日期',
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
    field: 'busiDate',
    title: '账务日期',
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
    field: 'inventoryCode',
    label: '盘点单号',
    component: 'Input',
    colProps: {
      span: 6,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'inventoryType',
    label: '盘点类型',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_INVENTORY_TYPE',
        disabled: true,
      };
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
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_INVENTORY_WAY',
        disabled: true,
      };
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
    field: 'inventoryStatus',
    label: '单据状态',
    required: true,
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_INV_INVENTORY_STATUS',
      disabled: true,
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
    field: 'busiDate',
    label: '账务日期',
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
    field: 'inventoryPlanDate',
    label: '计划盘点日期',
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
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 6,
    },
  },
];

export const appendFormSchema: FormSchema[] = [
  {
    field: 'itemCode',
    label: '物料编码',
    component: 'MaterialSelect',
    componentProps: ({ formActionType }) => {
      return {
        multiple: false,
        onSelect: (itemCode: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            itemName: rows?.itemName || '',
            itemSpec: rows?.itemSpec || '',
            unitCode: rows?.unitCode || '',
            drawingNo: rows?.drawingNo || '',
            itemCode: itemCode,
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
    field: 'itemName',
    label: '物料名称',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 24,
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
      span: 24,
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
      span: 24,
    },
  },
  {
    field: 'itemGroupCode',
    label: '物料组',
    component: 'ItemGroupSelect',
    componentProps: {
      class: '!w-full',
      disabled: true,
    },
    colProps: {
      span: 24,
    },
  },
  {
    field: 'lotNo',
    label: '批次号',
    required: true,
    slot: 'lotNo',
    componentProps: ({ formModel }) => {
      return {
        disabled: !formModel.itemCode,
      };
    },
    colProps: {
      span: 24,
    },
  },
];

export const executeSearchFormSchema: VxeFormItemProps[] = [
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
    field: 'lotNo',
    title: '批次号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    span: 6,
    className: '!pr-0',
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

export const detailColumns: VxeGridPropTypes.Columns = [
  { title: '序号', type: 'seq', width: 50, align: 'center' },
  { title: '行号', field: 'lineNo', width: 80, sortable: true },
  { title: '物料编码', field: 'itemCode', width: 100 },
  { title: '物料名称', field: 'itemName', width: 100 },
  { title: '规格型号', field: 'itemSpec', width: 100 },
  { title: '单位', field: 'unitCode', formatter: 'dictText', width: 80 },
  { title: '图号', field: 'drawingNo', width: 100 },
  { title: '物料组', field: 'itemGroupCode', formatter: 'dictText', width: 80 },
  { title: '批次号', field: 'lotNo', width: 120 },
  { title: '批次索引号', field: 'subLotIndex', width: 100 },
  { title: '库存数量', field: 'quantityStr', width: 100 },
  { title: '实盘数量', field: 'inventoryNum', width: 100 },
  { title: '账实差异', field: 'inventoryDiffNum', width: 100 },
  {
    title: '处理方式',
    field: 'handleMethod',
    slots: {
      default: ({ row }) => {
        const handleMethod = row.handleMethod;
        if (!handleMethod) {
          return '';
        }
        let color = 'green';
        if (handleMethod === '-1') {
          color = '#f50';
        } else if (handleMethod === '1') {
          color = 'red';
        }
        return h(
          Tag,
          {
            color: color,
          },
          () => row.textMap?.handleMethod$DICT_TEXT_ || '',
        );
      },
    },
    width: 100,
  },
  { title: '差异原因', field: 'diffReason', width: 100, sortable: true },
  {
    title: '责任单位',
    field: 'responsibleUnit',
    formatter: 'dictText',
    width: 100,
    sortable: true,
  },
  { title: '备注', field: 'memo', width: 100 },
];

export const checkFormSchema: FormSchema[] = [
  {
    field: 'reviewOpinion',
    label: '复核意见',
    required: true,
    component: 'Select',
    componentProps: () => {
      return {
        options: [
          { label: '同意', value: '01' },
          { label: '驳回', value: '03' },
        ],
      };
    },
    colProps: {
      span: 6,
    },
  },
  {
    field: 'reviewResult',
    label: '复核结果',
    component: 'InputTextArea',
    colProps: {
      span: 6,
    },
  },
];

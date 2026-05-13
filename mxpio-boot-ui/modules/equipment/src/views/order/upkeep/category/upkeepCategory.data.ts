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
  { title: '编码', field: 'code' },
  { title: '名称', field: 'name' },
  { title: '是否启用', field: 'isEnable', formatter: 'dictText' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'code',
    title: '编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'name',
    title: '名称',
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
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    field: 'num',
    width: 60,
  },
  {
    title: '内容',
    field: 'content',
  },
  {
    title: '内容类型',
    field: 'contentType',
    formatter: 'dictText',
  },
  {
    title: '属性值单位',
    field: 'attributeUnit',
  },
  {
    title: '存在选项',
    field: 'chooseEnable',
    formatter: 'dictText',
  },
  {
    title: '部位描述',
    field: 'placeDescribe',
  },
  {
    title: '方法',
    field: 'method',
  },
  {
    title: '目标',
    field: 'target',
  },
  {
    title: '是否记录属性',
    field: 'isRecord',
    formatter: 'dictText',
  },
  {
    title: '上限值',
    field: 'upperLimit',
  },
  {
    title: '下限值',
    field: 'lowerLimit',
  },
  {
    title: '告警阈值',
    field: 'warnValue',
  },
  {
    title: '预警阈值',
    field: 'earlyWarnValue',
  },
  {
    title: '估计用时(分钟)',
    field: 'evaluateTime',
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const detailFormSchema: FormSchema[] = [
  {
    field: 'num',
    label: '序号',
    component: 'Input',
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'paramId',
    label: 'paramId',
    component: 'Input',
    ifShow: false,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'content',
    label: '内容',
    component: 'BizSelect',
    componentProps: ({ formActionType }) => {
      return {
        columns: [
          {
            title: '名称',
            align: 'center',
            field: 'paramName',
            filterRender: {
              name: 'AInput',
            },
            filters: [{ data: '' }],
          },
          {
            title: '单位',
            align: 'center',
            field: 'unit',
            filterRender: {
              name: 'AInput',
            },
            filters: [{ data: '' }],
          },
          {
            title: '分类',
            align: 'center',
            field: 'orderType',
            filterRender: {
              name: 'DictSelect',
              props: {
                dictCode: 'ERP_EQUIPMENT_COMM_ORDER_TYPE',
                mode: 'multiple',
                operator: 'IN',
              },
            },
            filters: [{ data: [] }],
            formatter: 'dictText',
          },
        ],
        listUrl: '/erp/equipment/parameters/listPage/20',
        rowKey: 'id',
        displayKey: 'paramName',
        title: '保养内容',
        multiple: false,
        onSelect: (val: string, rows: Recordable) => {
          const { setFieldsValue } = formActionType;
          setFieldsValue({
            chooseEnable: rows[0]?.chooseEnable.toString() || '',
            contentType: rows[0]?.paramType || '',
            attributeUnit: rows[0]?.unit || '',
            upperLimit: rows[0]?.max || '',
            lowerLimit: rows[0]?.min || '',
            paramId: rows[0]?.id || '',
            method: rows[0]?.method || '',
            target: rows[0]?.remarks || '',
            content: rows[0]?.paramName || '',
          });
        },
      };
    },
    required: true,
    colProps: {
      span: 8,
    },
  },
  {
    field: 'contentType',
    label: '内容类型',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_EQUIPMENT_COMM_TYPE',
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'attributeUnit',
    label: '属性值单位',
    component: 'Input',
    componentProps: {
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'chooseEnable',
    label: '存在选项',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_YESNO',
      disabled: true,
    },
    colProps: {
      span: 8,
    },
  },
  {
    field: 'placeDescribe',
    label: '部位描述',
    component: 'Input',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'method',
    label: '方法',
    component: 'Input',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'isRecord',
    label: '是否记录属性',
    component: 'DictSelect',
    componentProps: {
      dictCode: 'ERP_COMMON_YESNO',
    },
    colProps: {
      span: 8,
    },
    required: true,
    defaultValue: '1',
  },
  {
    field: 'upperLimit',
    label: '上限值',
    component: 'InputNumber',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'lowerLimit',
    label: '下限值',
    component: 'InputNumber',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'warnValue',
    label: '告警阈值',
    component: 'InputNumber',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'evaluateTime',
    label: '估计用时(分钟)',
    component: 'InputNumber',
    colProps: {
      span: 8,
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
      span: 8,
    },
  },
];

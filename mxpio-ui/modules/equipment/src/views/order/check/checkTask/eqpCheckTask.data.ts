import type { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    type: 'radio',
    width: 40,
  },
  { type: 'seq', width: 40 },
  {
    title: '工单编码',
    field: 'bizNo',
    width: 120,
  },
  {
    title: '点检计划编码',
    field: 'planCode',
    width: 120,
  },
  {
    title: '点检计划名称',
    field: 'planName',
    width: 120,
  },
  {
    title: '资产名称',
    field: 'targetName',
    width: 120,
  },
  {
    title: '资产编码',
    field: 'targetId',
    width: 120,
    slots: { default: 'detail' },
  },
  { title: '父级资产编码', field: 'parentTargetId', width: 120 },
  { title: '父级资产名称', field: 'parentTargetName', width: 120 },
  {
    title: '规格型号',
    field: 'targetSpec',
    formatter: 'dictText',
    width: 120,
  },
  {
    title: '使用单位',
    field: 'useDeptId',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '设备分类',
    field: 'eqpTypeId',
    formatter: 'dictText',
    width: 100,
  },

  {
    title: '设备等级',
    field: 'codeAbc',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '工单状态',
    field: 'orderStatus',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '计划开始时间',
    field: 'planStartTime',
    width: 150,
  },
  {
    title: '失效截止时间',
    field: 'expCutOffTime',
    width: 150,
  },
  {
    title: '是否失效',
    field: 'whetherInvalid',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '自动下达',
    field: 'isAutoRelease',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '计划下达时间',
    field: 'planReleaseTime',
    width: 150,
  },
  {
    title: '实际开始时间',
    field: 'actualStartTime',
    width: 150,
  },
  {
    title: '实际结束时间',
    field: 'actualEndTime',
    width: 150,
  },
  {
    title: '否决原因',
    field: 'reasonsRej',
    width: 150,
  },
  {
    title: '点检负责人',
    field: 'executor',
    formatter: 'dictText',
    width: 100,
  },
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
    title: '工单编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'targetId',
    title: '资产编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'targetName',
    title: '资产名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'planCode',
    title: '点检计划编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
    folding: true,
  },
  {
    field: 'planName',
    title: '点检计划名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
    folding: true,
  },
  {
    field: 'orderStatus@EQ',
    title: '工单状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_CHECKTASK_STATUS',
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

export const detailColumns: VxeGridPropTypes.Columns = [
  {
    type: 'checkbox',
    width: 40,
  },
  {
    title: '序号',
    field: 'num',
    width: '50',
    align: 'center',
  },
  {
    title: '内容',
    field: 'content',
    width: 120,
  },
  {
    title: '部位描述',
    field: 'placeDescribe',
    width: 120,
  },
  {
    title: '方法',
    field: 'method',
    width: 100,
  },
  {
    title: '目标',
    field: 'target',
    width: 100,
  },
  {
    title: '是否记录属性',
    field: 'isRecord',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '上限值',
    field: 'upperLimit',
    width: 100,
  },
  {
    title: '下限值',
    field: 'lowerLimit',
    width: 100,
  },
  {
    title: '内容类型',
    field: 'contentType',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '存在选项',
    align: 'center',
    field: 'chooseEnable',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '告警阈值',
    field: 'warnValue',
    width: 100,
  },
  {
    title: '预警阈值',
    field: 'earlyWarnValue',
    width: 100,
  },
  {
    title: '属性单位',
    field: 'attributeUnit',
    width: 100,
  },
  {
    title: '估计用时（分钟）',
    field: 'evaluateTime',
    width: 100,
  },
  {
    title: '标准图片',
    field: 'pic',
    slots: { default: 'pic' },
    width: 100,
  },
  {
    title: '实际值',
    field: 'actualVaule',
    width: 100,
  },
  {
    title: '是否异常',
    field: 'isAbnormal',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '异常描述',
    field: 'abnormalRemark',
    width: 100,
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
    width: 100,
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
        listUrl: '/erp/equipment/parameters/listPage/10',
        rowKey: 'id',
        displayKey: 'paramName',
        title: '公共参数',
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
            target: rows[0]?.target || '',
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
    field: 'target',
    label: '目标',
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
    field: 'warnValue',
    label: '告警阈值',
    component: 'InputNumber',
    colProps: {
      span: 8,
    },
  },
  {
    field: 'earlyWarnValue',
    label: '预警阈值',
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
    label: '标准图片',
    component: 'Upload',
    componentProps: {
      accept: ['image/*'],
    },
    colProps: {
      span: 8,
    },
  },
];

export const transferColumns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '原执行者',
    field: 'oldExecutor',
  },
  {
    title: '现执行者',
    field: 'newExecutor',
  },
  {
    title: '变更原因',
    field: 'reason',
  },
];

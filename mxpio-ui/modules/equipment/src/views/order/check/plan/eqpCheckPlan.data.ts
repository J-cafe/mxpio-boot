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
  { title: '计划编码', field: 'code', width: 150 },
  { title: '计划名称', field: 'name', width: 100 },
  { title: '计划状态', field: 'execStatus', formatter: 'dictText', width: 100 },
  { title: '计划类型', field: 'planType', formatter: 'dictText', width: 100 },
  { title: '目标类型', field: 'targetType', formatter: 'dictText', width: 100 },
  { title: '开始日期', field: 'startTime', width: 120 },
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
        if (row.endLoopType === '2') {
          text = row.endTimes;
        } else if (row.endLoopType === '3') {
          text = row.endDate;
        }
        return text;
      },
    },
  },
  { title: '是否自动下达', field: 'isAuto', formatter: 'dictText', width: 120 },
  { title: '提前下达时间(天)', field: 'advanceTime', width: 120 },
  { title: '任务开始时间', field: 'taskPlanStartTime', width: 120 },
  { title: '生效状态', field: 'isEnable', formatter: 'dictText', width: 100 },
  { title: '是否暂停', field: 'pauseGenerateTask', width: 100, formatter: 'dictText' },
  { title: '备注', field: 'memo', width: 100 },
  { title: '下次执行日期', field: 'lastPlanEnd', width: 150 },
  {
    title: '操作',
    field: 'operation',
    fixed: 'right',
    slots: { default: 'action' },
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
    title: '编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'execStatus@EQ',
    title: '计划状态',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_PLAN_EXEC_STATUS',
      },
    },
    span: 6,
  },
  {
    field: 'pauseGenerateTask@EQ',
    title: '是否暂停',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
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
    title: '标准图片',
    field: 'pic',
    slots: { default: 'pic' },
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    fixed: 'right',
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

export const targetColumns: VxeGridPropTypes.Columns = [
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
    title: '资产台账编码',
    field: 'targetId',
  },
  {
    title: '资产名称',
    field: 'targetName',
  },
  {
    title: '设备编码',
    field: 'eqpCode',
  },
  { title: '父级资产编码', field: 'parentTargetId' },
  { title: '父级资产名称', field: 'parentTargetName' },
  { title: '目标规格', field: 'targetSpec' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const targetByCategoryFormSchema: FormSchema[] = [
  {
    field: 'categoryId',
    label: '类别',
    component: 'EqpCategorySelect',
    required: true,
    colProps: {
      span: 24,
    },
  },
];

export const detailByCategoryFormSchema: FormSchema[] = [
  {
    field: 'categoryId',
    label: '模版',
    component: 'ApiSelectPage',
    componentProps: () => {
      return {
        url: '/erp/equipment/spotcheck/tpmCheckCategory/page',
        optionKey: { label: 'name', value: 'id' },
      };
    },
    required: true,
    colProps: {
      span: 24,
    },
  },
];

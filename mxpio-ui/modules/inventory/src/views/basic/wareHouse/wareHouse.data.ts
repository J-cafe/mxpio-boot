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
  {
    title: '仓库编号',
    field: 'whCode',
    width: 140,
    sortable: true,
  },
  {
    title: '仓库名称',
    field: 'whName',
  },
  {
    title: '所属部门',
    field: 'deptCode',
    formatter: 'dictText',
  },
  {
    title: '仓管员',
    field: 'keeper',
    formatter: 'dictText',
  },
  {
    title: '仓库属性',
    field: 'whType',
    formatter: 'dictText',
  },
  {
    title: '核算类型',
    field: 'settleStatus',
    formatter: 'dictText',
  },
  {
    title: '计价类型',
    field: 'whPricing',
    formatter: 'dictText',
  },
  {
    title: '是否生效',
    field: 'whStatus',
    formatter: 'dictText',
  },
  {
    title: '是否参与MRP',
    field: 'mrpStatus',
    formatter: 'dictText',
  },
  {
    title: '备注',
    field: 'memo',
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
    field: 'deptCode',
    title: '所属部门',
    itemRender: {
      name: 'DeptSelect',
    },
    span: 6,
  },
  {
    field: 'whCode',
    title: '仓库编号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'keeper@EQ',
    title: '仓管员',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
      },
    },
    span: 6,
  },
  {
    field: 'whType@EQ',
    title: '仓库属性',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_WH_TYPE',
      },
    },
    span: 6,
  },
  {
    field: 'settleStatus@EQ',
    title: '核算类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
    },
    span: 6,
  },
  {
    field: 'whPricing@EQ',
    title: '计价类型',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_INV_WH_PRICING',
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
    field: 'whCode',
    label: '仓库编号',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'whName',
    label: '仓库名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'deptCode',
    label: '所属部门',
    component: 'DeptSelect',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'keeper',
    label: '仓管员',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'whType',
    label: '仓库属性',
    required: true,
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_WH_TYPE',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'whPricing',
    label: '计价类型',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_INV_WH_PRICING',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'settleStatus',
    label: '是否核算',
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
    field: 'whStatus',
    label: '是否生效',
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
    field: 'mrpStatus',
    label: '是否参与MRP',
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
    field: 'memo',
    label: '备注',
    component: 'InputTextArea',
    colProps: {
      span: 12,
    },
  },
];

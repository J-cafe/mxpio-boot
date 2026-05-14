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
    title: '客户编码',
    field: 'pnCode',
    width: 140,
    sortable: true,
  },
  {
    title: '客户名称',
    field: 'pnName',
  },
  {
    title: '简称',
    field: 'pnAbbr',
  },
  {
    title: '业务员',
    field: 'bizMan',
    formatter: 'dictText',
  },
  {
    title: '地区',
    field: 'pnArea',
    formatter: 'dictText',
  },
  {
    title: '联系人',
    field: 'pnContacts',
  },
  {
    title: '手机',
    field: 'pnPhone',
  },
  {
    title: '电话',
    field: 'pnTel',
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
    field: 'pnCode',
    title: '客户编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'pnName',
    title: '客户名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'pnArea@EQ',
    title: '地区',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_PARTNER_AREA',
      },
    },
    span: 6,
  },
  {
    field: 'bizMan@EQ',
    title: '业务员',
    itemRender: {
      name: 'UserByDeptSelect',
      props: {
        multiple: false,
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
    field: 'pnCode',
    label: '客户编码',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'pnName',
    label: '客户名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnAbbr',
    label: '简称',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'bizMan',
    label: '业务员',
    component: 'UserByDeptSelect',
    componentProps: {
      multiple: false,
    },
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnArea',
    label: '地区',
    component: 'DictSelect',
    componentProps: () => {
      return {
        dictCode: 'ERP_COMMON_PARTNER_AREA',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnKey',
    label: '社会统一信用代码',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnContacts',
    label: '联系人',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnPhone',
    label: '手机',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnTel',
    label: '电话',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnFax',
    label: '传真',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnAddress',
    label: '公司地址',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
  {
    field: 'pnZipCode',
    label: '邮政编码',
    component: 'Input',
    colProps: {
      span: 12,
    },
  },
];

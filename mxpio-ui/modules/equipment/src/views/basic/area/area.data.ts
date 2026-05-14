import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  { title: '区域名称', field: 'areaName', treeNode: true },
  { title: '区域编码', field: 'areaCode' },
  { title: '厂区', field: 'factory', formatter: 'dictText' },
  { title: '是否启用', field: 'isEnable', formatter: 'dictText' },
  { title: '描述', field: 'remarks' },
  { title: '创建人', field: 'createBy', formatter: 'dictText' },
  { title: '创建日期', field: 'createTime' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'areaName',
    title: '区域名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'areaCode',
    title: '区域编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'factory@EQ',
    title: '厂区',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_FACTORY',
      },
    },
    span: 6,
  },
  {
    field: 'isEnable@EQ',
    title: '是否启用',
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

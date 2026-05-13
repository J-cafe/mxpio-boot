import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';
import XEUtils from 'xe-utils';

export const columns: VxeGridPropTypes.Columns = [
  {
    title: '序号',
    type: 'seq',
    fixed: 'left',
    width: '50',
    align: 'center',
  },
  {
    title: '物料编码',
    field: 'itemCode',
    width: 120,
    sortable: true,
  },
  {
    title: '生命周期',
    field: 'lifeCycle',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '物料名称',
    field: 'itemName',
    width: 100,
  },
  {
    title: '规格型号',
    field: 'itemSpec',
    width: 100,
  },
  {
    title: '单位',
    field: 'unitCode',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '物料组',
    field: 'itemGroupCode',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '审核状态',
    field: 'bpmnStatus',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '图号',
    field: 'drawingNo',
    width: 100,
  },
  {
    title: '单价',
    field: 'itemPrice',
    width: 80,
  },
  {
    title: '物料来源',
    field: 'itemSource',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '是否虚拟件',
    field: 'virtualPart',
    formatter: 'dictText',
    width: 100,
  },
  {
    title: '是否原料',
    field: 'material',
    formatter: 'dictText',
    width: 80,
  },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
    fixed: 'right',
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
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
    field: 'drawingNo',
    title: '图号',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'itemSource@EQ',
    title: '物料来源',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_TECH_ITEM_SOURCE',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'virtualPart@EQ',
    title: '是否虚拟件',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
      attrs: { class: '!w-full' },
    },
    span: 6,
  },
  {
    field: 'material@EQ',
    title: '是否原料',
    folding: true,
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
      attrs: { class: '!w-full' },
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

export const drawingColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '文件名称',
    field: 'fileNo',
    editRender: {
      name: 'Upload',
      placeholder: '请选择',
      events: {
        change: (params: any, value, files) => {
          const { row } = params;
          const file = files?.[0];
          XEUtils.set(row, 'length', file?.['size']);
          XEUtils.set(row, 'fileName', file?.['name']);
          XEUtils.set(row, 'fileNo', value[0]);
        },
      },
    },
    cellRender: {
      name: 'Upload',
      props: {
        disabled: true,
      },
    },
  },
  {
    title: '文件名称',
    field: 'fileName',
  },
  {
    title: '文件大小',
    field: 'length',
  },
  {
    title: '版本',
    field: 'version',
  },
  {
    title: '版次',
    field: 'edition',
  },
  {
    title: '文件描述',
    field: 'remarks',
    editRender: {
      name: 'AInput',
    },
  },
  {
    title: '创建人',
    field: 'createBy',
    formatter: 'dictText',
  },
];

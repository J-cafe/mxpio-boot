import type { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

import XEUtils from 'xe-utils';

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
  { type: 'expand', width: 60, slots: { content: 'expand_content' } },
  { title: '参数名称', field: 'paramName' },
  { title: '参数类型', field: 'paramType', formatter: 'dictText' },
  { title: '参数分类', field: 'orderType', formatter: 'dictText' },
  { title: '单位', field: 'unit', formatter: 'dictText' },
  { title: '最大值', field: 'max' },
  { title: '最小值', field: 'min' },
  { title: '缺省值', field: 'defaultValue' },
  { title: '方法', field: 'method' },
  { title: '备注', field: 'remarks' },
  { title: '存在选项', field: 'chooseEnable', formatter: 'dictText' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'paramName',
    title: '参数名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'paramCode',
    title: '参数编码',
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
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    field: 'num',
    width: '50',
    align: 'center',
  },
  {
    title: '选项',
    field: 'parameter',
    editRender: {
      name: 'AInput',
      placeholder: '请点击输入',
    },
  },
  {
    title: '是否默认',
    field: 'isDefault',
    editRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_YESNO',
        events: {
          change: (params: any, value: string) => {
            const { row, $grid } = params;
            if (value === '1') {
              const { fullData } = $grid.getTableData();
              fullData.forEach((item) => {
                if (item.isDefault && item._X_ROW_KEY !== row._X_ROW_KEY) {
                  XEUtils.set(item, 'isDefault', '0');
                  XEUtils.set(item, 'textMap', {
                    ...item.textMap,
                    isDefault$DICT_TEXT_: '否',
                  });
                }
              });
            }
          },
        },
      },
    },
  },
];

import { VxeFormItemProps, VxeGridPropTypes } from '@mxpio/components';

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
  { title: '群组名称', field: 'groupName' },
  { title: '群组编码', field: 'groupCode', sortable: true },
  { title: '群组类别', field: 'groupCategory', formatter: 'dictText' },
  { title: '主管', field: 'directorId', formatter: 'dictText' },
  { title: '联系电话', field: 'groupTel' },
  { title: '是否启用', field: 'isEnable', formatter: 'dictText' },
  { title: '备注', field: 'remarks' },
  {
    title: '操作',
    field: 'operation',
    slots: { default: 'action' },
    width: 120,
  },
];

export const searchFormSchema: VxeFormItemProps[] = [
  {
    field: 'groupName',
    title: '群组名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'groupCode',
    title: '群组编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'groupCategory@EQ',
    title: '群组类别',
    itemRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_EQUIPMENT_GROUP_CATEGORY',
      },
    },
    span: 6,
  },
  {
    field: 'directorId@EQ',
    title: '主管',
    folding: true,
    itemRender: {
      name: 'UserByDeptSelect',
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

export const teamUserColumns: VxeGridPropTypes.Columns = [
  { type: 'checkbox', width: 40 },
  {
    title: '序号',
    type: 'seq',
    width: '50',
    align: 'center',
  },
  {
    title: '账号',
    field: 'userId',
    editRender: {
      name: 'UserByDeptSelect',
      placeholder: '请点击输入',
      events: {
        change: (params: any, value, items) => {
          const { row, $grid } = params;
          const insertData: Recordable[] = [];
          items.forEach((item: any, i) => {
            if (i === 0) {
              XEUtils.set(row, 'userId', item.username);
              XEUtils.set(row, 'workNo', item.nickname);
            } else {
              insertData.push({
                userId: item.username,
                workNo: item.nickname,
                isEnable: '1',
              });
            }
          });
          $grid.insert(insertData);
        },
      },
    },
  },
  {
    title: '姓名',
    field: 'workNo',
  },
  {
    title: '是否启用',
    field: 'isEnable',
    editRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'ERP_COMMON_YESNO',
      },
    },
  },
  {
    title: '备注',
    field: 'remarks',
    editRender: {
      name: 'Input',
    },
  },
];

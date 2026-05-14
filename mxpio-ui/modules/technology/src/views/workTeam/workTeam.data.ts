import { VxeFormItemProps, VxeGridPropTypes, FormSchema } from '@mxpio/components';
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
  {
    title: '班组编号',
    field: 'workTeamCode',
    width: 120,
    sortable: true,
  },
  {
    title: '班组名称',
    field: 'workTeamName',
  },
  {
    title: '班组类型',
    field: 'workTeamType',
    formatter: 'dictText',
  },
  {
    title: '所属车间',
    field: 'wkshopCode',
    formatter: 'dictText',
  },
  {
    title: '工作中心',
    field: 'workCenterCode',
    formatter: 'dictText',
  },
  {
    title: '班长',
    field: 'teamLeader',
  },
  {
    title: '是否启用',
    field: 'status',
    formatter: 'dictText',
  },
  {
    title: '备注',
    field: 'remark',
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
    field: 'workTeamCode',
    title: '班组编码',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workTeamName',
    title: '班组名称',
    itemRender: {
      name: 'AInput',
    },
    span: 6,
  },
  {
    field: 'workShopCode@EQ',
    title: '所属车间',
    itemRender: {
      name: 'WorkShopSelect',
    },
    span: 6,
  },
  {
    field: 'workCenterCode@EQ',
    title: '工作中心',
    folding: true,
    itemRender: {
      name: 'WorkCenterSelect',
      props: {
        alwaysLoad: true,
        filters: ({ data }) => {
          return {
            'workShopCode@EQ': data?.['workShopCode@EQ'] || null,
          };
        },
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
    field: 'workTeamCode',
    label: '班组编号',
    component: 'Input',
    colProps: {
      span: 12,
    },
    componentProps: {
      disabled: true,
    },
  },
  {
    field: 'workTeamName',
    label: '班组名称',
    component: 'Input',
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'workTeamType',
    label: '班组类型',
    component: 'DictSelect',
    required: true,
    componentProps: () => {
      return {
        dictCode: 'ERP_TECH_WORK_TEAM_TYPE',
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'wkshopCode',
    label: '所属车间',
    required: true,
    component: 'WorkShopSelect',
    componentProps: ({ formModel }) => {
      return {
        onChange: () => {
          formModel.workCenterCode = '';
        },
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'workCenterCode',
    label: '所属工作中心',
    required: true,
    component: 'WorkCenterSelect',
    componentProps: ({ formModel }) => {
      return {
        disabled: !formModel.wkshopCode,
        filters: {
          'workShopCode@EQ': formModel.wkshopCode || null,
        },
        alwaysLoad: true,
      };
    },
    colProps: {
      span: 12,
    },
  },
  {
    field: 'teamLeader',
    label: '班长',
    component: 'UserByDeptSelect',
    componentProps: () => {
      return {
        multiple: false,
      };
    },
    required: true,
    colProps: {
      span: 12,
    },
  },
  {
    field: 'status',
    label: '是否启用',
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
    field: 'memberId',
    editRender: {
      // name: 'AInput',
      name: 'UserByDeptSelect',
      placeholder: '请点击输入',
      events: {
        change: (params: any, value, items) => {
          const { row, $grid } = params;
          const insertData: Recordable[] = [];
          items.forEach((item: any, i) => {
            if (i === 0) {
              XEUtils.set(row, 'memberId', item.username);
              XEUtils.set(row, 'memberName', item.nickname);
            } else {
              insertData.push({
                memberId: item.username,
                memberName: item.nickname,
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
    field: 'memberName',
  },
];

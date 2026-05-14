import { BasicColumn, FormSchema, Icon } from '@mxpio/components';
import { DATASCOPEEnum } from '@mxpio/enums/src/menuEnum';
import { h } from 'vue';
import { Tag } from 'ant-design-vue';

export const columns: BasicColumn[] = [
  {
    title: '菜单标题',
    dataIndex: 'title',
    width: 200,
    align: 'left',
  },
  {
    title: '组件路径',
    dataIndex: 'component',
  },
  {
    title: '路由地址',
    dataIndex: 'path',
  },
  {
    title: '菜单范畴',
    dataIndex: 'urlScope',
    width: 80,
    customRender: ({ record }) => {
      const urlScope = record.urlScope;
      let tagColor = '';
      let tagText = '';
      switch (urlScope) {
        case 'platform':
          tagColor = '#108ee9';
          tagText = '平台';
          break;
        case 'modules':
          tagColor = '#2db7f5';
          tagText = '模块';
          break;
        case 'menu':
          tagColor = '#87d068';
          tagText = '菜单';
          break;
        default:
          return;
      }
      return tagColor ? h(Tag, { color: tagColor }, () => tagText) : '';
    },
  },
  {
    title: '图标',
    dataIndex: 'icon',
    width: 120,
    customRender: ({ record }) => {
      return record.icon ? h(Icon, { icon: record.icon }) : '';
    },
  },
  {
    title: '排序',
    dataIndex: 'order',
    width: 120,
  },
  {
    title: '是否显示',
    dataIndex: 'navigable',
    width: 80,
    customRender: ({ record }) => {
      const navigable = record.navigable;
      const enable = ~~navigable === 1;
      const color = enable ? 'green' : 'red';
      const text = enable ? '显示' : '隐藏';
      return h(Tag, { color: color }, () => text);
    },
  },
  {
    title: '创建时间',
    dataIndex: 'createTime',
    width: 120,
  },
];

// 权限列表
export const authColumns: BasicColumn[] = [
  {
    title: '名称',
    dataIndex: 'title',
    width: 120,
    align: 'left',
  },
  {
    title: '路径',
    dataIndex: 'path',
  },
  {
    title: '数据权限',
    dataIndex: 'hasCriteria',
  },
  {
    title: '权限标识',
    dataIndex: 'elementId',
    width: 80,
  },
  {
    title: '描述',
    dataIndex: 'description',
  },
];

// 数据过滤列表
export const datafilterColumns: BasicColumn[] = [
  {
    title: '前置处理器',
    dataIndex: 'preProcess',
  },
  {
    title: '权限范围',
    dataIndex: 'dataScope',
  },
  {
    title: '服务',
    dataIndex: 'service',
  },
  {
    title: '描述',
    dataIndex: 'description',
  },
];

// 数据过滤表单
export const datafilterFormSchema: FormSchema[] = [
  {
    field: 'preProcess',
    label: '前置处理器',
    component: 'Input',
  },
  {
    field: 'dataScope',
    label: '权限范围',
    component: 'Select',
    required: true,
    componentProps: {
      options: [
        {
          label: '按用户',
          value: DATASCOPEEnum.USER,
        },
        {
          label: '按部门',
          value: DATASCOPEEnum.DEPT,
        },
        {
          label: '部门及子部门',
          value: DATASCOPEEnum.DEPT_AND_CHILD,
        },
        {
          label: '按服务',
          value: DATASCOPEEnum.SERVICE,
        },
      ],
    },
  },
  {
    field: 'hasCriteria',
    label: '服务',
    component: 'Input',
    ifShow: ({ values }) => {
      return values.dataScope === DATASCOPEEnum.SERVICE;
    },
    required: true,
  },
  {
    field: 'description',
    label: '描述',
    component: 'InputTextArea',
  },
];

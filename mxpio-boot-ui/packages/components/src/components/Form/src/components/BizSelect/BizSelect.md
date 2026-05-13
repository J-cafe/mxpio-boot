# BizSelect 业务数据选择组件

## 概述

`BizSelect` 是一个基于弹窗表格的业务数据选择组件，支持单选/多选、远程数据加载、列过滤、自动回显等功能。适用于需要从大量业务数据中选择一条或多条记录的场景。

## 引入

```ts
import { BizSelect } from '@mxpio/components';
```

## 基础用法

### 单选模式

```vue
<BizSelect
  v-model:value="userId"
  :columns="columns"
  listUrl="/user/list"
  rowKey="username"
  displayKey="nickname"
  :multiple="false"
/>
```

### 多选模式（默认）

```vue
<BizSelect
  v-model:value="userIds"
  :columns="columns"
  listUrl="/user/list"
  rowKey="username"
  displayKey="nickname"
/>
```

### 在表单配置中使用

```ts
const columns = [
  {
    title: '名称',
    align: 'center',
    field: 'paramName',
    filterRender: { name: 'AInput' },
    filters: [{ data: '' }],
  },
  {
    title: '单位',
    align: 'center',
    field: 'unit',
    filterRender: { name: 'AInput' },
    filters: [{ data: '' }],
  },
];

const formSchema = [
  {
    field: 'content',
    label: '内容',
    component: 'BizSelect',
    componentProps: ({ formActionType }) => {
      return {
        columns,
        listUrl: '/erp/equipment/parameters/listPage/10',
        rowKey: 'id',
        displayKey: 'paramName',
        title: '公共参数',
        multiple: false,
        onSelect: (val: string, rows: Recordable[]) => {
          const { setFieldsValue } = formActionType;
          // 选中后自动填充其他表单字段
          setFieldsValue({
            unit: rows[0]?.unit || '',
            paramId: rows[0]?.id || '',
          });
        },
      };
    },
  },
];
```

## API

### Props

| 参数 | 说明 | 类型 | 默认值 |
| --- | --- | --- | --- |
| `value` (v-model) | 选中的值，多个值用逗号分隔的字符串 | `string` | - |
| `disabled` | 是否禁用 | `boolean` | `false` |
| `multiple` | 是否支持多选 | `boolean` | `true` |
| `displayKey` | 显示字段名，设置后自动根据 value 调用接口翻译显示文本 | `string` | `null` |
| `rowKey` | 行唯一标识字段名 | `string` | `'id'` |
| `listUrl` | 列表数据接口地址 | `string` | `''` |
| `columns` | 弹窗表格的列配置数组 | `VxeColumn[]` | `[]` |
| `title` | 弹窗标题后缀（显示为"请选择{title}"） | `string` | `''` |
| `isTransform` | 是否自动将 value 翻译为显示文本 | `boolean` | `true` |

### Events

| 事件名         | 说明                    | 回调参数                              |
| -------------- | ----------------------- | ------------------------------------- |
| `update:value` | 值变化时触发（v-model） | `(value: string, rows: Recordable[])` |
| `select`       | 选择完成后触发          | `(value: string, rows: Recordable[])` |

> **说明**：`select` 事件仅在组件内部选择操作时触发，外部修改 `value` 不会触发。可用于选中后联动填充其他表单字段。

### 列配置（columns）

`columns` 支持 VxeTable 的标准列配置，推荐配置 `filterRender` 和 `filters` 来启用列头过滤功能：

```ts
const columns = [
  {
    title: '用户账号',
    field: 'username',
    filterRender: { name: 'AInput' }, // 输入框过滤
    filters: [{ data: '' }],
  },
  {
    title: '分类',
    field: 'orderType',
    filterRender: {
      name: 'DictSelect', // 字典下拉过滤
      props: {
        dictCode: 'YOUR_DICT_CODE',
        mode: 'multiple',
        operator: 'IN',
      },
    },
    filters: [{ data: [] }],
    formatter: 'dictText', // 字典值格式化
  },
];
```

### filterRender 支持的类型

| name         | 说明             | 备注                              |
| ------------ | ---------------- | --------------------------------- |
| `AInput`     | 文本输入框过滤   | 对应 Ant Design Vue 的 Input 组件 |
| `DictSelect` | 字典下拉选择过滤 | 需配置 `props.dictCode`           |

## 工作机制

1. **初始回显**：组件挂载时，如果 `value` 有值且配置了 `displayKey`，会自动调用 `listUrl` 接口查询并显示对应的文本
2. **打开弹窗**：点击输入框打开弹窗，弹窗内展示数据表格，支持分页、列头过滤
3. **多选回显**：多选模式下，弹窗打开时会自动回显之前已选中的行（通过 `rowKey@IN` 查询接口）
4. **已选管理**：多选模式下，工具栏显示"已选择: N条"的气泡卡片，可查看和移除已选项
5. **确认选择**：点击确认后，将选中行的 `rowKey` 值用逗号拼接后通过 `v-model` 回传

## 完整示例

```vue
<template>
  <PageWrapper title="BizSelect 示例">
    <a-card>
      <!-- 单选 -->
      <BizSelect
        v-model:value="userId"
        :columns="columns"
        listUrl="/user/list"
        rowKey="username"
        displayKey="nickname"
        :multiple="false"
        title="用户"
      />

      <!-- 多选 + 选择回调 -->
      <BizSelect
        v-model:value="itemIds"
        :columns="itemColumns"
        listUrl="/item/list"
        rowKey="id"
        displayKey="itemName"
        @select="handleItemSelect"
        title="物料"
      />
    </a-card>
  </PageWrapper>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { PageWrapper, BizSelect } from '@mxpio/components';

  const userId = ref('');
  const itemIds = ref('');

  const columns = [
    {
      title: '用户账号',
      field: 'username',
      filterRender: { name: 'AInput' },
      filters: [{ data: '' }],
    },
    {
      title: '用户姓名',
      field: 'nickname',
      filterRender: { name: 'AInput' },
      filters: [{ data: '' }],
    },
  ];

  const itemColumns = [
    {
      title: '物料编码',
      field: 'itemCode',
      filterRender: { name: 'AInput' },
      filters: [{ data: '' }],
    },
    {
      title: '物料名称',
      field: 'itemName',
      filterRender: { name: 'AInput' },
      filters: [{ data: '' }],
    },
  ];

  function handleItemSelect(val: string, rows: any[]) {
    console.log('选中的值:', val);
    console.log('选中的行数据:', rows);
  }
</script>
```

## 注意事项

- `listUrl` 接口需遵循 `getAction` 的调用规范，返回结果需包含 `content` 数组字段
- `value` 为逗号分隔的字符串格式，如 `'id1,id2,id3'`
- 设置 `isTransform: false` 可关闭自动翻译显示文本的功能，此时输入框直接显示原始 `value`

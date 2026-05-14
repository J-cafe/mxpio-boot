# 字典自动加载使用说明

## 功能说明

VxeTable 的 `DictSelect` 渲染器现在支持**自动加载字典并缓存**，无需手动维护 `textMap`。

## 使用方式

### 1. 基本使用

在列配置中添加 `dictCode` 字段即可：

```typescript
const columns = [
  {
    field: 'status',
    title: '状态',
    // 编辑时：自动加载并显示字典下拉框
    editRender: {
      name: 'DictSelect',
      props: {
        dictCode: 'status', // 字典编码
      },
    },
  },
];
```

### 2. 工作原理

1. **首次渲染**：当单元格首次显示时，会自动触发字典加载请求
2. **自动缓存**：字典数据加载完成后，会缓存在内存中
3. **自动更新**：字典加载完成后，会自动调用 `$table.updateStatus()` 重新渲染单元格
4. **后续渲染**：再次渲染时直接从缓存获取，无需重复请求

### 3. 渲染优先级

渲染器按以下优先级获取显示文本：

1. **textMap**：向后兼容原有方式（已存在的代码不受影响）
2. **字典缓存**：从 `dictCode` 对应的缓存中获取
3. **options**：手动传入的选项数组
4. **原始值**：兜底返回 cellValue

### 4. 字典缓存服务 API

```typescript
import { dictCache } from '@/common/src/components/VxeTable/dictCache';

// 获取字典选项（自动缓存）
const options = await dictCache.get('status');

// 获取单个标签
const label = await dictCache.getLabel('status', '1'); // 返回 '正常'

// 获取多个标签（多选）
const labels = await dictCache.getLabels('status', ['1', '2']); // 返回 '正常,禁用'

// 同步获取（已缓存时使用）
const options = dictCache.getSync('status');
const label = dictCache.getLabelSync('status', '1');

// 预加载多个字典（在表格挂载时调用）
await dictCache.preload(['status', 'type', 'priority']);

// 手动设置字典
dictCache.set('status', [{ label: '正常', value: '1' }]);

// 清除缓存
dictCache.clear('status'); // 清除单个
dictCache.clear(); // 清除所有
```

### 5. 预加载字典（推荐）

在表格组件挂载时，可以预先加载所有字典，提升首屏加载体验：

```vue
<script setup lang="ts">
  import { onMounted } from 'vue';
  import { dictCache } from '@/common/src/components/VxeTable/dictCache';

  const columns = [
    {
      field: 'status',
      title: '状态',
      editRender: {
        name: 'DictSelect',
        props: {
          dictCode: 'status',
        },
      },
    },
    {
      field: 'type',
      title: '类型',
      editRender: {
        name: 'DictSelect',
        props: {
          dictCode: 'status',
        },
      },
    },
  ];

  onMounted(async () => {
    // 提取所有字典码
    const dictCodes = columns
      .filter((col) => col.cellRender?.dictCode || col.editRender?.dictCode)
      .map((col) => col.cellRender?.dictCode || col.editRender?.dictCode);

    // 预加载字典（去重）
    await dictCache.preload([...new Set(dictCodes)]);
  });
</script>
```

### 6. 向后兼容

如果现有代码使用了 `textMap` 方式，仍然可以正常工作，无需修改：

```typescript
// 原有方式：手动维护 textMap
const data = [
  {
    id: 1,
    status: '1',
    textMap: {
      status$DICT_TEXT_: '正常',
    },
  },
];

// 新方式：只需配置 dictCode
const columns = [
  {
    field: 'status',
    title: '状态',
    cellRender: {
      name: 'DictSelect',
      dictCode: 'status',
    },
  },
];
```

## 注意事项

1. **字典编码**：`dictCode` 必须与后端字典表中的字典编码一致
2. **首次加载**：首次渲染时会先显示原始值，加载完成后自动更新
3. **缓存策略**：字典数据缓存在内存中，刷新页面后需重新加载
4. **错误处理**：字典加载失败时，会在控制台输出警告，不影响表格正常显示

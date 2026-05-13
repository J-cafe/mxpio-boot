<template>
  <ApiTreeSelect
    class="w-full"
    :api="loadData"
    labelField="name"
    :valueField="valueField"
    v-bind="$attrs"
    :value="multiple ? value?.split(',') : value"
    :multiple="multiple"
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
  import { reactive } from 'vue';
  import { ApiTreeSelect } from '@mxpio/components';
  import { eqpCategoryTreeApi } from '../../../api/equipment/category';

  defineOptions({ name: 'EqpCategorySelect' });

  const emit = defineEmits(['update:value', 'change']);

  // 树形数据
  const dataSourceObj = reactive({});
  const valueField = 'code';
  defineProps({
    value: { type: String },
    multiple: { type: Boolean, default: false },
  });

  // 获取区域列表
  async function loadData() {
    const res = await eqpCategoryTreeApi();
    treeToObject(res);
    return res;
  }

  // 递归区域列表
  function treeToObject(list) {
    list.forEach((item) => {
      dataSourceObj[item[valueField]] = item;
      if (item.children.length > 0) {
        treeToObject(item.children);
      }
    });
  }

  // 选择事件
  function handleChange(value, label, extra) {
    const rows: Recordable[] = [];
    let values = value;
    if (!Array.isArray(value)) {
      values = [value];
    }
    values.forEach((value) => {
      rows.push(dataSourceObj[value]);
    });
    emit('update:value', values?.join(','), label, extra, rows);
    emit('change', values?.join(','), label, extra, rows);
  }
</script>

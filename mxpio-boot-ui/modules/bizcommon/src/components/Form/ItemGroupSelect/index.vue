<template>
  <ApiTreeSelect
    class="w-full"
    :api="loadData"
    labelField="groupName"
    valueField="groupCode"
    v-bind="$attrs"
    :value="multiple ? value?.split(',') : value"
    :multiple="multiple"
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
  import { reactive } from 'vue';
  import { ApiTreeSelect } from '@mxpio/components';
  import { itemGroup } from '../../../api/technology/material';

  defineOptions({ name: 'ItemGroupSelect' });

  const emit = defineEmits(['update:value', 'change']);

  // 树形数据
  const dataSourceObj = reactive({});
  defineProps({
    value: { type: String },
    multiple: { type: Boolean, default: false },
  });

  // 获取物料组列表
  async function loadData() {
    const res = await itemGroup();
    treeToObject(res);
    return res;
  }

  // 递归物料组列表
  function treeToObject(list) {
    list.forEach((item) => {
      dataSourceObj[item.groupCode] = item;
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

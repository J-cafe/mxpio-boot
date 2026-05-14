<template>
  <ApiTreeSelect
    class="w-full"
    :api="loadData"
    labelField="deptName"
    valueField="deptCode"
    :showSearch="true"
    treeNodeFilterProp="deptName"
    v-bind="$attrs"
    :value="multiple ? value?.split(',') : value"
    :multiple="multiple"
    @change="handleChange"
  />
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { ApiTreeSelect } from '@mxpio/components';
  import { deptList } from '@mxpio/api';

  defineOptions({ name: 'DeptSelect' });

  const emit = defineEmits(['update:value', 'change']);
  // 树形数据
  const deptObj = ref({});
  defineProps({
    value: { type: String },
    multiple: { type: Boolean, default: false },
  });

  // 获取部门列表
  async function loadData(queryParams) {
    const res = await deptList(queryParams);
    deptToObject(res);
    return res;
  }

  // 递归部门列表
  function deptToObject(list) {
    list.forEach((item) => {
      deptObj.value[item.deptCode] = item;
      if (item.children.length > 0) {
        deptToObject(item.children);
      }
    });
  }

  // 选择部门
  function handleChange(value, label, extra) {
    const depts: Recordable[] = [];
    let values = value;
    if (!Array.isArray(value)) {
      values = [value];
    }
    values.forEach((deptCode) => {
      depts.push(deptObj.value[deptCode]);
    });
    emit('update:value', values?.join(','), label, extra, depts);
    emit('change', values?.join(','), label, extra, depts);
  }
</script>

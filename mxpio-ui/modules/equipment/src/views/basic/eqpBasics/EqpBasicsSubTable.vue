<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions" :data="dataSource" />
</template>
<script lang="ts" setup>
  import { watch, ref } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { subColumns } from './eqpBasics.data';
  import { eqpBasicsSubApi } from '@mxpio/bizcommon';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'EqpBasicsSubTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    basicsCode: {
      type: String,
      default: () => '',
    },
  });

  const dataSource = ref([]);
  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => props.basicsCode,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const gridOptions = {
    tableClass: '!px-0 !py-0',
    height: 280,
    columns: subColumns,
    rowConfig: {
      keyField: 'basicsCode',
    },
    toolbarConfig: {
      import: false,
      buttons: [],
    },
    proxyConfig: {
      enabled: false,
    },
    treeConfig: {
      transform: true,
      rowField: 'basicsCode',
      parentField: 'parentCode',
      lazy: true,
      hasChild: 'hasSub',
      loadMethod: loadChildrenMethod,
    },
  };

  async function loadData() {
    try {
      if (props.basicsCode) {
        const res = await eqpBasicsSubApi(props.basicsCode);
        dataSource.value = res || [];
      } else {
        dataSource.value = [];
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function loadChildrenMethod({ row }) {
    try {
      const res = await eqpBasicsSubApi(row.basicsCode);
      const childs: Recordable[] = [];
      res.forEach((item) => {
        childs.push({
          ...item,
          bomId: row.bomLineId,
        });
      });
      return new Promise((resolve) => {
        resolve(childs);
      });
    } catch (error) {
      console.error('Error loading children:', error);
    }
  }
</script>

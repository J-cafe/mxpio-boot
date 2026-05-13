<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { detailColumns } from './bomAudit.data';

  defineOptions({ name: 'BomDetailTable' });

  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomDetailTable',
      keepSource: true,
      minHeight: '200px',
      columns: detailColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      'cell-class-name': ({ row }) => {
        if (row.tmpCrudType === CuryTypeEnum.SAVE || !row.tmpCrudType) {
          return 'row--add';
        }
      },
      'row-class-name': ({ row }) => {
        if (row.tmpCrudType === CuryTypeEnum.DELETE) {
          return 'row--pending';
        }
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    const record = data.record || {};
    dataSource.value = record.bomInfoLineList;
    formData.value = record;
    tableRef.value?.loadData(dataSource.value);
  }

  async function validate() {
    let errMap = await tableRef.value?.validate(true);
    if (errMap) {
      return Promise.reject(errMap);
    }
    return Promise.resolve();
  }

  defineExpose({
    setData,
    validate,
  });
</script>
<style lang="less">
  .row--add {
    background-color: #f1e3c9 !important;
  }

  .row--pending {
    text-decoration: none;
  }
</style>

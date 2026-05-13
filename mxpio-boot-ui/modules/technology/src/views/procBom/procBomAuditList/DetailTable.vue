<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { CuryTypeEnum } from '@mxpio/enums';
  import { detailColumns } from './procBomAudit.data';
  import { procBomAuditLine } from '@mxpio/bizcommon';

  defineOptions({ name: 'DetailTable' });

  const isDisabled = ref(false);
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const formData: Recordable = {};

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DetailTable',
      keepSource: true,
      minHeight: '300px',
      tableClass: '!px-0 !py-0',
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
      sortConfig: {
        trigger: 'cell',
        remote: false,
        defaultSort: { field: 'processOrder', order: 'asc' },
      },
    };
  });

  async function setData(data: Recordable) {
    isDisabled.value = !!data?.disabled;
    const record = data.record || {};
    try {
      const res = await procBomAuditLine(record.routId);
      dataSource.value = res;
      // dataSource.value = record.lineList;
      formData.value = record;
      tableRef.value?.loadData(dataSource.value);
    } catch (error) {
      console.log(error);
    }
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

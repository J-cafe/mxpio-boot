<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="死循环物料"
    :showOkBtn="false"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { findCircularReferenceBoms } from '@mxpio/bizcommon';
  import { endLessColumns } from './bom.data';

  defineOptions({ name: 'BomEndlessModal' });

  const [registerModal] = useModalInner(async () => {
    loadData();
  });

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomEndLessTable',
      keepSource: true,
      minHeight: '200px',
      showOverflow: 'tooltip',
      columns: endLessColumns,
      proxyConfig: {
        ajax: {
          query: async () => {
            return loadData();
          },
        },
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  async function loadData() {
    try {
      const res = await findCircularReferenceBoms();
      return res;
    } catch (error) {
      console.error(error);
    }
  }
</script>

<template>
  <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions" />
</template>
<script lang="ts" setup>
  import { ref, computed, watch } from 'vue';
  import { VxeBasicTable } from '@mxpio/components';
  import { listSubBom } from '@mxpio/bizcommon';
  import type { BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { subColumns } from './bomStorey.data';
  import { dateUtil } from '@mxpio/utils';

  defineOptions({ name: 'BomStoreyTree' });
  const props = defineProps({
    parentCode: {
      type: String,
      default: '',
    },
    parentData: {
      type: Object,
      default: () => ({}),
    },
  });
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);

  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomStoreyTree',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: subColumns,
      data: dataSource.value,
      proxyConfig: {
        autoLoad: false,
        ajax: {
          query: async () => {
            return loadData();
          },
        },
      },
      pagerConfig: {
        enabled: false,
      },
      treeConfig: {
        transform: true,
        rowField: 'itemCode',
        parentField: 'bomId',
        lazy: true,
        hasChild: 'hasSub',
        loadMethod: loadChildrenMethod,
      },
    };
  });

  async function loadData() {
    if (!props.parentCode) {
      return [];
    }
    const res = await listSubBom({
      itemCode: props.parentCode,
      date: dateUtil(props.parentData.endTime).add(-1, 'day').format('YYYY-MM-DD'),
    });
    return res;
  }

  async function loadChildrenMethod({ row }) {
    if (!row.itemCode) {
      return [];
    }
    const res = await listSubBom({
      itemCode: row.itemCode,
      date: dateUtil(props.parentData.endTime).add(-1, 'day').format('YYYY-MM-DD'),
    });
    return res;
  }

  watch(
    () => props.parentCode,
    () => {
      if (props.parentCode) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    },
  );
</script>

<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @checkbox-change="checkboxChange"
    @checkbox-all="checkboxChange"
  >
    <template #action="{ row }">
      <TableAction
        :outside="true"
        :actions="[
          {
            label: '编辑',
            onClick: handleEdit.bind(null, row),
            auth: auth.edit,
          },
          {
            label: '删除',
            color: 'error',
            popConfirm: {
              title: '是否确认删除',
              placement: 'left',
              confirm: handleDelete.bind(null, row),
            },
            auth: auth.delete,
          },
        ]"
      />
    </template>
    <template #pic="{ row }">
      <a v-if="row.pic" @click="handleDownload(row)">下载</a>
    </template>
  </VxeBasicTable>
  <EqpInfoBomModal @register="registerModal" @success="handleSuccess" :eqpCode="props.eqpCode" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction } from '@mxpio/components';
  import { bomColumns } from './eqpInfo.data';
  import { eqpBomPageApi, deleteEqpBomApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useCommon } from '@mxpio/hooks';
  import { useDebounceFn } from '@vueuse/core';
  import EqpInfoBomModal from './EqpInfoBomModal.vue';

  const componentName = 'EqpInfoBomTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    eqpCode: {
      type: String,
      default: () => '',
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => props.eqpCode,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const { downloadByFileNo } = useCommon();
  const {
    tableRef,
    auth,
    gridOptions,
    handleEdit,
    handleDelete,
    registerModal,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns: bomColumns,
    pageApi: eqpBomPageApi,
    deleteApi: deleteEqpBomApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: '300px',
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        import: false,
      },
      proxyConfig: {
        enabled: false,
      },
    },
    filters: () => ({
      'eqpCode@EQ': props.eqpCode,
    }),
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.eqpCode) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function handleDownload(row) {
    console.log(row);
    try {
      await downloadByFileNo(row.pic);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }
</script>

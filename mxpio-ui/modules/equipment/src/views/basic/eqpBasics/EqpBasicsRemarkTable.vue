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
  </VxeBasicTable>
  <EqpBasicsRemarkModal
    @register="registerModal"
    @success="handleSuccess"
    :basicsCode="props.basicsCode"
  />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction } from '@mxpio/components';
  import { remarkColumns } from './eqpBasics.data';
  import { eqpRemarkPageApi, deleteEqpRemarkApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';
  import EqpBasicsRemarkModal from './EqpBasicsRemarkModal.vue';

  const componentName = 'EqpBasicsRemarkTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    basicsCode: {
      type: String,
      default: () => '',
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => props.basicsCode,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
    },
  );
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
    columns: remarkColumns,
    pageApi: eqpRemarkPageApi,
    deleteApi: deleteEqpRemarkApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: 280,
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
      'basicsCode@EQ': props.basicsCode,
    }),
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.basicsCode) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }
</script>

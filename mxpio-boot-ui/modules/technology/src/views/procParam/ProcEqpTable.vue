<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions">
    <template #action="{ row }">
      <TableAction
        :outside="true"
        :actions="[
          {
            label: '编辑',
            onClick: handleEdit.bind(null, row),
            auth: auth.edit,
          },
        ]"
        :dropDownActions="[
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
  <ProcEqpModal
    width="900px"
    :prodrout="prodrout"
    @register="registerModal"
    @success="handleSuccess"
  />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction } from '@mxpio/components';
  import { procEqpColumns } from './procParam.data';
  import { procEqpList, deleteProcEqp } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import ProcEqpModal from './ProcEqpModal.vue';

  const componentName = 'ProcEqpTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    prodrout: {
      type: Object,
      default: () => ({}),
    },
  });

  watch(
    () => props.prodrout,
    () => {
      loadData();
    },
    {
      deep: true,
    },
  );

  const { tableRef, auth, gridOptions, registerModal, handleEdit, handleDelete, handleSuccess } =
    useListCrudHook({
      componentName,
      columns: procEqpColumns,
      pageApi: procEqpList,
      deleteApi: deleteProcEqp,
      vxeGridOptions: {
        tableClass: '!px-0 !py-0',
        height: '300px',
        rowConfig: {
          keyField: 'id',
        },
        toolbarConfig: {
          import: false,
        },
        pagerConfig: {
          enabled: false,
        },
        proxyConfig: {
          enabled: false,
        },
      },
      filters() {
        return {
          'routProcId@EQ': props.prodrout?.id || null,
        };
      },
      module: 'erp',
    });

  async function loadData() {
    try {
      if (props.prodrout.id) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }
</script>

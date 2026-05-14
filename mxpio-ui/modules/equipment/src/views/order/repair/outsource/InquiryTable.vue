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
            ifShow: row.isUse === '0',
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
            ifShow: row.isUse === '0',
          },
          {
            label: '确认',
            onClick: handleConfirm.bind(null, row),
            ifShow: row.isUse === '0',
            auth: authConfig.confirm,
          },
        ]"
      />
    </template>
  </VxeBasicTable>
  <InquiryModal @register="registerModal" @success="handleSuccess" :bizNo="props.bizNo" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction } from '@mxpio/components';
  import { inquiryColumns } from './outsource.data';
  import {
    repairOutsourceInquiryPageApi,
    deleteRepairOutsourceInquiryApi,
    repairOutsourceInquiryAdoptApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';
  import InquiryModal from './InquiryModal.vue';

  const componentName = 'InquiryTable';
  defineOptions({ name: componentName });

  const emit = defineEmits(['ok']);
  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
    task: {
      type: Object,
      default: () => {},
    },
  });

  const authConfig = {
    confirm: `erp:${componentName}:confirm`,
  };

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => [props.bizNo, props.task.orderStatus],
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
    columns: inquiryColumns,
    pageApi: repairOutsourceInquiryPageApi,
    deleteApi: deleteRepairOutsourceInquiryApi,
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
      'bizNo@EQ': props.bizNo,
    }),
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.bizNo) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function handleConfirm(row: Recordable) {
    try {
      await repairOutsourceInquiryAdoptApi(row);
      handleSuccess();
      emit('ok');
    } catch (error) {
      console.error('Error confirming inquiry:', error);
    }
  }
</script>

<template>
  <div>
    <VxeBasicTable class="!p-0" ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleEdit.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BPMNTaskModal :width="1200" type="view" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { finishedList } from '@mxpio/bizcommon';
  import { finishedColumns, searchFinishedFormSchema } from './myTask.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import BPMNTaskModal from './BPMNTaskModal.vue';

  defineOptions({ name: 'MyFinishedTask' });

  const [registerModal, { openModal }] = useModal();

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'MyFinishedTask',
    columns: finishedColumns,
    formConfig: {
      enabled: true,
      items: searchFinishedFormSchema,
    },
    minHeight: 700,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
    // sortConfig: {
    //   defaultSort: [
    //     {
    //       field: 'procStartTime',
    //       order: 'asc',
    //     },
    //   ],
    // },
  });

  function loadData(queryParams: VxeTableQueryParams) {
    const params = getVxeTableQueryParams(queryParams);
    return finishedList(params);
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: false,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>

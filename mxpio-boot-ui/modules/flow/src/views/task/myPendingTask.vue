<template>
  <div>
    <VxeBasicTable class="!p-0" ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: row.taskType === 'active' ? '办理' : '拾取',
              onClick: handleEdit.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BPMNTaskModal :width="1200" type="handle" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { taskList } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './myTask.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import BPMNTaskModal from './BPMNTaskModal.vue';

  defineOptions({ name: 'MyPendingTask' });

  const [registerModal, { openModal }] = useModal();

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'myPendingTask',
    columns: columns,
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    minHeight: 700,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
    sortConfig: {
      defaultSort: [
        {
          field: 'bpmnSortFlag',
          order: 'desc',
        },
        {
          field: 'procStartTime',
          order: 'asc',
        },
      ],
    },
  });

  function loadData(queryParams: VxeTableQueryParams) {
    const params = getVxeTableQueryParams(queryParams);
    return taskList(params);
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>

<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '查看',
              onClick: handleView.bind(null, row),
            },
            {
              label: '暂停',
              onClick: handleSuspend.bind(null, row),
              ifShow: row.state === 'ACTIVE',
            },
            {
              label: '开始',
              onClick: handleStart.bind(null, row),
              ifShow: row.state === 'SUSPENDED',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BPMNTaskModal :width="1200" type="view" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { processList, suspend, start } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './process.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import BPMNTaskModal from '../task/BPMNTaskModal.vue';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'ProcessList' });

  const [registerModal, { openModal }] = useModal();
  const { createMessage } = useMessage();

  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'ProcessList',
    columns: columns,
    toolbarConfig: {
      import: false,
    },
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
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await processList(params);
    return res;
  }

  function handleView(record: Recordable) {
    openModal(true, {
      isUpdate: false,
      record: {
        ...record,
        processInstanceId: record.id,
      },
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  async function handleSuspend(row: Recordable) {
    try {
      await suspend(row.id);
      tableRef.value?.commitProxy('query');
      createMessage.success('暂停成功');
    } catch (error) {
      console.log(error);
    }
  }

  async function handleStart(row: Recordable) {
    try {
      await start(row.id);
      tableRef.value?.commitProxy('query');
      createMessage.success('开始成功');
    } catch (error) {
      console.log(error);
    }
  }
</script>

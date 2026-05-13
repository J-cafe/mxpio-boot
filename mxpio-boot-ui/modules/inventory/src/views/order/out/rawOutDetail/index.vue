<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <ExecuteModal @register="registerExecuteModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { wolinePageApi, woIsJitApI } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './rawOutDetail.data';
  import ExecuteModal from './ExecuteModal.vue';
  import { usePermission, useMessage } from '@mxpio/hooks';

  const componentName = 'RawOutDetailList';
  defineOptions({ name: componentName });
  const { hasPermission } = usePermission();
  const { createMessage } = useMessage();
  const [registerExecuteModal, { openModal: openExecuteModal }] = useModal();

  const authConfig = {
    execute: `erp:${componentName}:execute`,
  };

  const { tableRef, gridOptions, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: wolinePageApi,
    module: 'erp',
    filters: {
      'woType@IN': '40,41,42,43,44,45,46,47,48,50,56,58,62,64,86,83',
      'closeStatus@EQ': 'open',
    },
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [
          {
            content: '执行',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:page-next-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: () => handleExecute(),
              },
            },
            visible: hasPermission(authConfig.execute),
          },
        ],
        import: false,
        export: false,
      },
    },
  });

  async function handleExecute() {
    const selectedRows = tableRef.value?.getCheckboxRecords();
    if (!selectedRows || selectedRows.length === 0) {
      createMessage.error('请选择要执行的行');
      return;
    }
    const bizNos = selectedRows.map((item) => item.bizNo);
    const res = await woIsJitApI(bizNos.join(','));
    if (res) {
      createMessage.error('JIT出库单不能手动执行');
    }
    openExecuteModal(true, {
      rows: selectedRows,
    });
  }
</script>

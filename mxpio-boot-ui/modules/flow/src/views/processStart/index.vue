<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '发起流程',
              onClick: handleEdit.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ProcessStartModal :width="900" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, BasicVxeTableProps, VxeGridInstance } from '@mxpio/components';
  import { bpmnList } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './processStart.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import ProcessStartModal from './ProcessStartModal.vue';

  defineOptions({ name: 'ProcessStartList' });

  const [registerModal, { openModal }] = useModal();

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'ProcessStartList',
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
  });

  function loadData(queryParams: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      ...queryParams,
      filters: {
        'status@IN': '02,03',
        'visible@EQ': true,
      },
    });
    return bpmnList(params);
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

<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '审核',
              ifShow: (_action) => {
                return row.bpmnStatus === '01';
              },
              onClick: handleEdit.bind(null, row),
              auth: auth.audit,
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ProcDrawingfileAuditModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { drawingfileSigninPage } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './ProcDrawingfileAudit.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile } from '@mxpio/hooks';
  import ProcDrawingfileAuditModal from './ProcDrawingfileAuditModal.vue';

  defineOptions({ name: 'ProcDrawingfileAuditList' });

  const auth = {
    audit: 'erp:ProcDrawingfileAuditList:audit',
  };

  const [registerModal, { openModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'ProcDrawingfileAuditList',
    columns: columns,
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    customConfig: {
      storage: {
        visible: true,
        resizable: true,
        sort: true,
        fixed: true,
      }, // 启用自定义列状态保存功能
      restoreStore: restoreStore,
      updateStore: updateStore,
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
    const res = await drawingfileSigninPage(params);
    return res;
  }

  async function handleEdit(record: Recordable) {
    openModal(true, {
      record,
    });
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      disabled: true,
      isUpdate: true,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>

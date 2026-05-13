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
                return row.useType === '02';
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
    <ProcBomAuditModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { procBomAuditPage } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './procBomAudit.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile } from '@mxpio/hooks';
  import ProcBomAuditModal from './ProcBomAuditModal.vue';

  defineOptions({ name: 'ProcBomAuditList' });

  const auth = {
    audit: 'erp:ProcBomAuditList:audit',
  };

  const [registerModal, { openModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'ProcBomAuditList',
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
    const res = await procBomAuditPage(params);
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

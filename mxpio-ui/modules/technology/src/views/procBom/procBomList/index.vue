<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: row.version ? '变更' : '维护',
              onClick: handleEdit.bind(null, row),
              auth: auth.edit,
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '草稿',
              onClick: handleTmpEdit.bind(null, row),
              auth: auth.edit,
            },
            {
              label: '复制',
              onClick: handleCopy.bind(null, row),
              auth: auth.edit,
            },
            {
              label: '历史版本',
              onClick: handleHistory.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ProcBomModal width="80%" @register="registerModal" @success="handleSuccess" />
    <ProcBomHistoryModal width="80%" @register="registerHistoryModal" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { procBomPage, getTmpProcBom } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './procBom.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile, usePermission } from '@mxpio/hooks';
  import ProcBomModal from './ProcBomModal.vue';
  import ProcBomHistoryModal from './ProcBomHistoryModal.vue';

  const componentName = 'ProcBomList';
  defineOptions({ name: componentName });
  const [registerModal, { openModal }] = useModal();
  const [registerHistoryModal, { openModal: openHistoryModal }] = useModal();
  const { restoreStore, updateStore } = useProfile();
  const { hasPermission } = usePermission();
  const tableRef = ref<VxeGridInstance>();
  const filters = {
    'primaryRout@EQ': 1,
  };
  const auth = {
    add: `erp:${componentName}:add`,
    edit: `erp:${componentName}:edit`,
    delete: `erp:${componentName}:delete`,
    export: `erp:${componentName}:export`,
  };
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'ProcBomList',
    columns: columns,
    toolbarConfig: {
      buttons: [
        {
          content: '新增',
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
              click: () => {
                handleCreate();
              },
            },
          },
          visible: hasPermission(auth.add),
        },
      ],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: auth.export,
            },
          },
        },
      ],
      import: false,
    },
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
    const params = getVxeTableQueryParams({ page, form, sorts, filters });
    const res = await procBomPage(params);
    return res;
  }

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  async function handleEdit(record: Recordable) {
    const res = await getTmpProcBom(record.productItemCode);
    if (res && res.routId) {
      //已有编辑中草稿
      openModal(true, {
        record,
        isUpdate: true,
        isTmp: true,
      });
      return;
    }
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  async function handleTmpEdit(record: Recordable) {
    const res = await getTmpProcBom(record.productItemCode);
    if (res && res.routId) {
      //已有编辑中草稿
      openModal(true, {
        record,
        isUpdate: true,
        isTmp: true,
      });
      return;
    }
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      disabled: true,
      isUpdate: true,
    });
  }

  async function handleCopy(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
      isCopy: true,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  function handleHistory(record: Recordable) {
    openHistoryModal(true, {
      record,
    });
  }
</script>

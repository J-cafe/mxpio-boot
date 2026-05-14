<template>
  <div class="m-3">
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
    <WorkshopModal width="900px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, onBeforeMount, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { deleteWorkshop, workshopPage, workshopConfig } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './workshop.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile, usePermission, useMessage } from '@mxpio/hooks';
  import WorkshopModal from './WorkshopModal.vue';

  const componentName = 'WorkshopList';
  defineOptions({ name: componentName });

  const auth = {
    add: `erp:${componentName}:add`,
    edit: `erp:${componentName}:edit`,
    delete: `erp:${componentName}:delete`,
    import: `erp:${componentName}:import`,
  };

  const { hasPermission } = usePermission();
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();
  const importCode = ref();
  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'WorkshopList',
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
        {
          content: '删除',
          buttonRender: {
            name: 'AButton',
            props: {
              type: 'primary',
              danger: true,
              preIcon: 'mdi:delete-forever',
            },
            attrs: {
              class: 'ml-2',
            },
            events: {
              click: () => {
                handleBatchDel();
              },
            },
          },
          visible: hasPermission(auth.delete),
        },
      ],
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'mr-2',
            },
            props: {
              export: 'sys:WorkshopList:export',
            },
          },
        },
        {
          toolRender: {
            name: 'ImportButton',
            props: {
              importCode: () => importCode.value,
            },
          },
          visible: hasPermission(auth.import),
        },
      ],
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
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await workshopPage(params);
    return res;
  }

  function handleCreate() {
    openModal(true, {
      isUpdate: false,
    });
  }

  function handleEdit(record: Recordable) {
    openModal(true, {
      record,
      isUpdate: true,
    });
  }

  function handleDelete(record: Recordable) {
    deleteWorkshop(record.workShopCode).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  function handleBatchDel() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (selectedRows.length === 0) {
      createMessage.error('请选择要删除的数据');
      return;
    }
    createConfirm({
      title: '是否确认删除',
      iconType: 'warning',
      centered: false,
      onOk: () => {
        const selectedRowKeys = selectedRows.map((item) => item.workShopCode);
        deleteWorkshop(selectedRowKeys.join()).then(() => {
          tableRef.value?.commitProxy('query');
        });
      },
    });
  }

  onBeforeMount(async () => {
    const res = await workshopConfig();
    if (res) {
      importCode.value = res.importTemplate;
    }
  });
</script>

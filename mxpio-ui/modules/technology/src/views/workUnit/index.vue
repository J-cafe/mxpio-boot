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
    <WorkingUnitModal width="900px" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref, reactive, onBeforeMount } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { deleteWorkunit, workunitPage, workunitConfig } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './workUnit.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile, usePermission } from '@mxpio/hooks';
  import WorkingUnitModal from './WorkUnitModal.vue';

  const componentName = 'WorkUnitList';
  defineOptions({ name: componentName });

  const auth = {
    add: `erp:${componentName}:add`,
    edit: `erp:${componentName}:edit`,
    delete: `erp:${componentName}:delete`,
    import: `erp:${componentName}:import`,
  };
  const [registerModal, { openModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();
  const { hasPermission } = usePermission();
  const importCode = ref();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'WorkUnitList',
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
              export: 'sys:WorkUnitList:export',
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
    const res = await workunitPage(params);
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
    deleteWorkunit(record.workUnitCode).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  onBeforeMount(async () => {
    try {
      const res = await workunitConfig();
      if (res) {
        importCode.value = res.importTemplate;
      }
    } catch (error) {
      console.error('Failed to fetch importCode:', error);
    }
  });
</script>

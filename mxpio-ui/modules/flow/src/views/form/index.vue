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
            },
          ]"
          :dropDownActions="[
            {
              label: '设计表单',
              onClick: handleDesign.bind(null, row),
            },
            {
              label: '部署表单',
              onClick: handleDeploy.bind(null, row),
            },
            {
              label: '预览',
              onClick: handleView.bind(null, row),
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              ifShow: (_action) => {
                return row.status === '01';
              },
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <FormModal :width="900" @register="registerModal" @success="handleSuccess" />
    <FormDesignModal @register="registerDesignModal" @success="handleSuccess" />
    <FormViewModal :width="900" @register="registerViewModal" />
  </div>
</template>
<script lang="ts" setup>
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';

  import { ref, reactive } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { deleteForm, formList, deployForm } from '@mxpio/bizcommon';

  import { columns, searchFormSchema } from './form.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { message } from 'ant-design-vue';
  import FormModal from './FormModal.vue';
  import FormDesignModal from './FormDesignModal.vue';
  import FormViewModal from './FormViewModal.vue';
  import { useProfile } from '@mxpio/hooks';

  defineOptions({ name: 'FormList' });

  const [registerModal, { openModal }] = useModal();

  const [registerDesignModal, { openModal: openDesignModal }] = useModal();

  const [registerViewModal, { openModal: openViewModal }] = useModal();

  const { restoreStore, updateStore } = useProfile();
  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'FormList',
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
        },
      ],
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
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await formList(params);
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
    deleteForm(record.code).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  async function handleDeploy(record: Recordable) {
    message.loading('部署中，请稍后...', 0);
    try {
      await deployForm(record.code);
      message.destroy();
      tableRef.value?.commitProxy('query');
    } catch (error) {
      message.destroy();
      console.log(error);
    }
  }

  function handleDesign(record: Recordable) {
    openDesignModal(true, {
      record,
    });
  }

  function handleView(record: Recordable) {
    openViewModal(true, {
      record,
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>

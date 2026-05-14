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
              label: '设计流程',
              onClick: handleDesign.bind(null, row),
            },
            {
              label: '部署流程',
              onClick: handleDeploy.bind(null, row),
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
    <BpmnModal :width="900" @register="registerModal" @success="handleSuccess" />
    <BpmnDesignModal @register="registerDesignModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { deleteBpmn, bpmnList, deployBpmn } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './bpmn.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { useProfile } from '@mxpio/hooks';

  import { message } from 'ant-design-vue';
  import BpmnModal from './BpmnModal.vue';
  import BpmnDesignModal from './BpmnDesignModal.vue';
  import { reactive, ref } from 'vue';

  defineOptions({ name: 'BpmnList' });

  const [registerModal, { openModal }] = useModal();

  const [registerDesignModal, { openModal: openDesignModal }] = useModal();
  const { restoreStore, updateStore } = useProfile();

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'bpmnList',
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
      tools: [
        {
          toolRender: {
            name: 'ExportButton',
            attrs: {
              class: 'ml-2',
            },
            props: {
              export: 'sys:BpmnManagement:export',
            },
          },
        },
        {
          toolRender: {
            name: 'ImportButton',
            props: {
              importCode: 'bpmn',
            },
          },
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
    console.log(page, form, sorts);
    const params = getVxeTableQueryParams({ page, form, sorts });
    const res = await bpmnList(params);
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
    deleteBpmn(record.code).then(() => {
      tableRef.value?.commitProxy('query');
    });
  }

  async function handleDeploy(record: Recordable) {
    message.loading('部署中，请稍后...', 0);
    try {
      await deployBpmn(record.code);
      message.destroy();
      message.success('部署成功');
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

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }
</script>

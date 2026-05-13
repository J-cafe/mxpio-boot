<template>
  <div>
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <WithOutItemModal @register="registerModal" @success="handleSuccess" />
    <BatchEditModal
      :pnCode="currentRow.pnCode"
      :itemCodes="itemCodes"
      @register="registerBatchEditModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { itemColumns } from './bySupply.data';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { supplychainSupplyRemove } from '@mxpio/bizcommon';
  import WithOutItemModal from './WithOutItemModal.vue';
  import BatchEditModal from './BatchEditModal.vue';

  defineOptions({ name: 'WithInItem' });
  const emit = defineEmits(['success']);

  const props = defineProps({
    currentRow: {
      type: Object,
      default: () => ({}),
    },
  });

  const { hasPermission } = usePermission();
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();
  const [registerBatchEditModal, { openModal: openBatchEditModal }] = useModal();
  const tableRef = ref<VxeGridInstance>();
  const auth = {
    add: `erp:SupplyChainsBySupply:add`,
    edit: `erp:SupplyChainsBySupply:edit`,
    delete: `erp:SupplyChainsBySupply:delete`,
  };
  const itemCodes = ref<string>('');
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'WithInItemTable',
      tableClass: '!px-0 !py-0',
      columns: itemColumns,
      toolbarConfig: {
        buttons: [
          {
            content: '关联',
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
                  handleAdd();
                },
              },
            },
            visible: hasPermission(auth.add) && !!props.currentRow.pnCode,
          },
          {
            content: '批量修改',
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
                  handleEdit();
                },
              },
            },
            visible: hasPermission(auth.edit) && !!props.currentRow.pnCode,
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
              events: {
                click: () => {
                  handleBatchDel();
                },
              },
            },
            visible: hasPermission(auth.delete) && !!props.currentRow.pnCode,
          },
        ],
        tools: [],
      },
      proxyConfig: {
        enabled: false,
      },
      data: props.currentRow.supplyChains || [],
      pagerConfig: {
        enabled: false,
      },
    };
  });

  function handleSuccess() {
    emit('success');
  }

  function handleAdd() {
    openModal(true, {
      pnCode: props.currentRow.pnCode,
    });
  }

  function handleEdit() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (selectedRows.length === 0) {
      createMessage.error('请选择要修改的数据');
      return;
    }
    itemCodes.value = selectedRows.map((item) => item.itemCode).join(',');
    openBatchEditModal(true, {
      isUpdate: true,
    });
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
        supplychainSupplyRemove(
          props.currentRow.pnCode,
          selectedRows.map((item) => item.itemCode).join(','),
        ).then(() => {
          createMessage.success('删除成功');
          emit('success');
        });
      },
    });
  }
</script>

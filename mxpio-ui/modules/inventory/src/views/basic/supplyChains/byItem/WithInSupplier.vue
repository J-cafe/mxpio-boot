<template>
  <div>
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
    <WithOutSupplierModal @register="registerModal" @success="handleSuccess" />
    <BatchEditModal
      :itemCode="currentRow.itemCode"
      :pnCodes="pnCodes"
      @register="registerBatchEditModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { supplyColumns } from './byItem.data';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { supplychainItemRemove } from '@mxpio/bizcommon';
  import WithOutSupplierModal from './WithOutSupplierModal.vue';
  import BatchEditModal from './BatchEditModal.vue';

  defineOptions({ name: 'WithInSupplier' });
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
    add: `erp:SupplyChainsByItem:add`,
    edit: `erp:SupplyChainsByItem:edit`,
    delete: `erp:SupplyChainsByItem:delete`,
  };
  const pnCodes = ref<string>('');
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'WithInSupplierTable',
      tableClass: '!px-0 !py-0',
      columns: supplyColumns,
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
            visible: hasPermission(auth.add) && !!props.currentRow.itemCode,
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
            visible: hasPermission(auth.edit) && !!props.currentRow.itemCode,
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
            visible: hasPermission(auth.delete) && !!props.currentRow.itemCode,
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
      itemCode: props.currentRow.itemCode,
    });
  }

  function handleEdit() {
    const selectedRows: Recordable[] = tableRef.value?.getCheckboxRecords() || [];
    if (selectedRows.length === 0) {
      createMessage.error('请选择要修改的数据');
      return;
    }
    pnCodes.value = selectedRows.map((item) => item.pnCode).join(',');
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
        supplychainItemRemove(
          props.currentRow.itemCode,
          selectedRows.map((item) => item.pnCode).join(','),
        ).then(() => {
          createMessage.success('删除成功');
          emit('success');
        });
      },
    });
  }
</script>

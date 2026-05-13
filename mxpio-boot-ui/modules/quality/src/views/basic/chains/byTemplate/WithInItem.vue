<template>
  <div>
    <VxeBasicTable ref="tableRef1" v-bind="gridOptions" />
    <WithOutItemModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { useListCrudHook } from '@mxpio/common';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { itemColumns } from './byTemplate.data';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { qtChainItemWithinApi, qtChainRemoveApi } from '@mxpio/bizcommon';
  import WithOutItemModal from './WithOutItemModal.vue';
  import { watch } from 'vue';

  const componentName = 'QualityChainsWithInItem';
  defineOptions({ name: componentName });

  const props = defineProps({
    currentRow: {
      type: Object,
      default: () => ({}),
    },
  });

  const { hasPermission } = usePermission();
  const { createMessage, createConfirm } = useMessage();
  const [registerModal, { openModal }] = useModal();

  const authConfig = {
    add: `erp:QualityChainsByTemplate:add`,
    edit: `erp:QualityChainsByTemplate:edit`,
    delete: `erp:QualityChainsByTemplate:delete`,
  };

  watch(
    () => props.currentRow,
    () => {
      if (!props.currentRow?.code) {
        tableRef.value?.loadData([]);
        return;
      }
      tableRef.value?.commitProxy('query');
    },
    {
      deep: true,
    },
  );

  const { tableRef, gridOptions, handleSuccess } = useListCrudHook({
    componentName,
    columns: itemColumns,
    pageApi: (params) => {
      return qtChainItemWithinApi(props.currentRow.code, params);
    },
    vxeGridOptions: {
      rowConfig: {
        keyField: 'itemCode',
      },
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
            visible: hasPermission(authConfig.add),
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
            visible: hasPermission(authConfig.delete),
          },
        ],
        export: false,
        import: false,
      },
      proxyConfig: {
        autoLoad: false,
      },
      formConfig: {
        enabled: false,
      },
      tableClass: '!px-0 !py-0',
      height: 700,
    },
    module: 'erp',
  });

  function handleAdd() {
    if (!props.currentRow?.code) {
      createMessage.error('请先选择主数据');
      return;
    }
    openModal(true, {
      code: props.currentRow.code,
      busiType: props.currentRow.busiType,
    });
  }

  function handleBatchDel() {
    if (!props.currentRow?.code) {
      createMessage.error('请先选择主数据');
      return;
    }
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
        qtChainRemoveApi(
          props.currentRow.code,
          selectedRows.map((item) => item.itemCode).join(','),
          '1',
        ).then(() => {
          handleSuccess();
        });
      },
    });
  }
</script>

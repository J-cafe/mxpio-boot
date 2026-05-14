<template>
  <div>
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @checkbox-change="checkboxChange"
      @checkbox-all="checkboxChange"
    />
    <WithOutDetailModal @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { watch, computed } from 'vue';
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { forecastDetailColumns } from './bySale.data';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { spofDeleteApi, spofPageApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import WithOutDetailModal from './WithOutForecastDetailModal.vue';

  const componentName = 'WithInForecastDetail';
  defineOptions({ name: componentName });
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

  const auth = {
    add: `erp:SupplyChainsBySale:add`,
    edit: `erp:SupplyChainsBySale:edit`,
    delete: `erp:SupplyChainsBySale:delete`,
  };

  // 使用 computed 响应式计算按钮配置
  const vxeGridOptions = computed<BasicVxeTableProps>(() => {
    return {
      toolbarConfig: {
        buttons: [
          {
            content: '冲销',
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
            visible: hasPermission(auth.add) && !!props.currentRow.bizNo,
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
              attrs: {
                class: 'ml-2',
              },
            },
            visible: hasPermission(auth.delete) && !!props.currentRow.bizNo,
          },
        ],
        tools: [],
        export: false,
        import: false,
      },
      sortConfig: {
        defaultSort: {
          field: 'itemCode',
          order: 'desc',
        },
      },
      height: 700,
    };
  });

  const { tableRef, gridOptions, checkboxChange } = useListCrudHook({
    componentName,
    columns: forecastDetailColumns,
    pageApi: spofPageApi,
    vxeGridOptions: vxeGridOptions,
    filters: () => {
      return {
        'itemCode@EQ': props.currentRow.itemCode,
        'soBizNo@EQ': props.currentRow.bizNo,
        'soBizLineNo@EQ': props.currentRow.lineNo,
      };
    },
    module: 'erp',
  });

  function handleSuccess() {
    emit('success');
  }

  function handleAdd() {
    openModal(true, props.currentRow);
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
        spofDeleteApi(selectedRows.map((item) => item.id).join(',')).then(() => {
          emit('success');
        });
      },
    });
  }

  watch(
    () => props.currentRow,
    () => {
      tableRef.value?.commitProxy('query');
    },
    {
      deep: true,
    },
  );
</script>

<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @checkbox-change="checkboxChange"
    @checkbox-all="checkboxChange"
  >
    <template #action="{ row }">
      <TableAction
        :outside="true"
        :actions="[
          {
            label: '编辑',
            onClick: handleEdit.bind(null, row),
            auth: auth.edit,
            ifShow: !['3', '4'].includes(props.plan.execStatus),
          },
          {
            label: '删除',
            color: 'error',
            popConfirm: {
              title: '是否确认删除',
              placement: 'left',
              confirm: handleDelete.bind(null, row),
            },
            auth: auth.delete,
            ifShow: !['3', '4'].includes(props.plan.execStatus),
          },
        ]"
      />
    </template>
  </VxeBasicTable>
  <TargetAddModal
    width="80%"
    @register="registerModal"
    @success="handleSuccess"
    :planId="props.planId"
  />
  <TargetEditModal
    width="600px"
    @register="registeEditModal"
    @success="handleSuccess"
    :planId="props.planId"
  />
  <TargetByCategoryModal
    width="600px"
    @register="registeAddByCategoryrModal"
    @success="handleSuccess"
    :planId="props.planId"
  />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction, useModal } from '@mxpio/components';
  import { targetColumns } from './upkeepPlan.data';
  import { eqpUpkeepPlanTargetPageApi, deleteEqpUpkeepPlanTargetApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useDebounceFn } from '@vueuse/core';
  import TargetAddModal from './TargetAddModal.vue';
  import TargetEditModal from './TargetEditModal.vue';
  import TargetByCategoryModal from './TargetByCategoryModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'TargetTable';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const props = defineProps({
    planId: {
      type: String,
      default: () => '',
    },
    plan: {
      type: Object,
      default: () => {},
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  const [registeAddByCategoryrModal, { openModal: openAddByCategoryModal }] = useModal();
  const [registeEditModal, { openModal: openEditModal }] = useModal();
  watch(
    () => props.planId,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
    },
  );

  const {
    tableRef,
    auth,
    gridOptions,
    handleDelete,
    registerModal,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns: targetColumns,
    pageApi: eqpUpkeepPlanTargetPageApi,
    deleteApi: deleteEqpUpkeepPlanTargetApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: 280,
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        import: false,
      },
      proxyConfig: {
        enabled: false,
      },
    },
    filters: () => ({
      'planId@EQ': props.planId,
    }),
    appendButtons: [
      {
        content: '按类别新增',
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
            click: () => handleAddByCategory(),
          },
        },
      },
    ],
    addBefore,
    deleteBefore: addBefore,
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.planId) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function handleAddByCategory() {
    try {
      await addBefore();
      openAddByCategoryModal(true, {
        isUpdate: false,
      });
    } catch (error) {
      console.error('Error adding by category:', error);
    }
  }

  async function handleEdit(row) {
    try {
      openEditModal(true, {
        isUpdate: true,
        record: row,
      });
    } catch (error) {
      console.error('Error editing:', error);
    }
  }

  async function addBefore() {
    try {
      if (!props.planId) {
        createMessage.warning('请选择主记录！');
        return Promise.reject();
      }
      if (['3', '4'].includes(props.plan.execStatus)) {
        createMessage.warning('当前状态不操作');
        return Promise.reject();
      }
    } catch (error) {
      console.error('Error adding by category:', error);
    }
  }
</script>

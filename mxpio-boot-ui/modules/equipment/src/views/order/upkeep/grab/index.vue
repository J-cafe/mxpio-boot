<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" @data-change="onDataChange">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '抢单',
              onClick: handleGrab.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <UpkeepTaskModal width="80%" @register="registerModal" />
    <UpkeepTaskRejectModal width="600px" @register="registerRejectModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { eqpUpkeepTaskPageApi, grabEqpUpkeepApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './grab.data';
  import UpkeepTaskModal from '../task/UpkeepTaskModal.vue';
  import UpkeepTaskRejectModal from '../task/UpkeepTaskRejectModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'EqpUpkeepTaskGrabList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});
  const { createMessage } = useMessage();
  const [registerRejectModal, { openModal: openRejectModal }] = useModal();
  const { tableRef, gridOptions, handleDetail, registerModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpUpkeepTaskPageApi,
    vxeGridOptions: {
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [
          {
            content: '否决',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:close-circle-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: () => handleReject(),
              },
            },
          },
        ],
      },
    },
    filters: {
      'orderStatus@EQ': 20,
    },
    module: 'erp',
  });

  const handleGrab = async (row) => {
    try {
      await grabEqpUpkeepApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  };

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setRadioRow(visibleData[0]);
  }

  function handleReject() {
    if (!currentRow.value.bizNo) {
      createMessage.error('请选择点检任务');
      return;
    }

    openRejectModal(true, {
      bizNo: currentRow.value.bizNo,
    });
  }
</script>

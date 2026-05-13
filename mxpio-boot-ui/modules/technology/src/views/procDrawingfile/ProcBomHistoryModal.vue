<template>
  <BasicModal
    width="1000px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="历史版本"
    cancelText="关闭"
    :showOkBtn="false"
  >
    <VxeBasicTable class="pt-0" ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '工艺图纸',
              onClick: handleDetail.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <DesignFileHistoryModal @register="registerHistoryModal" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    TableAction,
    useModal,
  } from '@mxpio/components';
  import { procBomHistoryList } from '@mxpio/bizcommon';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { historyColumns } from './procDrawingfile.data';
  import DesignFileHistoryModal from './DesignFileHistoryModal.vue';

  defineOptions({ name: 'ProcBomHistoryModal' });
  const itemCode = ref('');
  const [registerHistoryModal, { openModal }] = useModal();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ProcBomHistoryTable',
      keepSource: true,
      height: '400px',
      columns: historyColumns,
      proxyConfig: {
        ajax: {
          query: async () => {
            return loadData();
          },
        },
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    itemCode.value = data.record.productItemCode;
    loadData();
  });

  async function loadData() {
    try {
      const res = await procBomHistoryList(itemCode.value);
      return res;
    } catch (error) {
      console.error(error);
    }
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      disabled: true,
      isUpdate: true,
    });
  }
</script>

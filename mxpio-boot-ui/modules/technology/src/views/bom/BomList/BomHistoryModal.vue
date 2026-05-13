<template>
  <BasicModal
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
              label: '查看',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '多阶',
              onClick: handleStorey.bind(null, row),
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BomModal @register="registerBomModal" />
    <BomStoreyTreeModal @register="registerBomStoreyTreeModal" />
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
  import { bomHistoryList } from '@mxpio/bizcommon';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { historyColumns } from './bom.data';
  import BomModal from './BomModal.vue';
  import BomStoreyTreeModal from '../BomStorey/BomStoreyTreeModal.vue';

  defineOptions({ name: 'BomHistoryModal' });
  const itemCode = ref('');
  const [registerBomModal, { openModal }] = useModal();
  const [registerBomStoreyTreeModal, { openModal: openBomStoreyTreeModal }] = useModal();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'BomHistoryTable',
      keepSource: true,
      minHeight: '200px',
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
    itemCode.value = data.record.parentCode;
    loadData();
  });

  async function loadData() {
    try {
      const res = await bomHistoryList(itemCode.value);
      return res;
    } catch (error) {
      console.error(error);
    }
  }

  function handleDetail(record: Recordable) {
    openModal(true, {
      record,
      disabled: true,
    });
  }

  function handleStorey(record: Recordable) {
    openBomStoreyTreeModal(true, {
      record,
    });
  }
</script>

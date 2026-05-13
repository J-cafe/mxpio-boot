<template>
  <div class="m-3">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      :checkbox-config="{ checkMethod: checCheckboxkMethod }"
      @checkbox-change="checkboxChange"
      @checkbox-all="checkboxChange"
    />
    <ToPurchaseModal @register="registerModal" @success="handleSuccess" />
    <BuyRequestRejectModal :rows="rows" @register="registerRejectModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { VxeBasicTable, useModal } from '@mxpio/components';
  import { brLineAuditedPage, supplyListByItem } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './buyRequestDetail.data';
  import BuyRequestRejectModal from './BuyRequestRejectModal.vue';
  import ToPurchaseModal from './ToPurchaseModal.vue';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { ref } from 'vue';

  const componentName = 'BuyRequestDetail';
  defineOptions({ name: componentName });
  const rows = ref<Recordable[]>([]);
  const [registerRejectModal, { openModal: openRejectModal }] = useModal();
  const { hasPermission } = usePermission();
  const { createMessage } = useMessage();

  const auth = {
    toPurchase: 'erp:BuyRequestDetail:toPurchase',
    reject: 'erp:BuyRequestDetail:reject',
  };

  const { tableRef, gridOptions, registerModal, openModal, handleSuccess, checkboxChange } =
    useListCrudHook({
      componentName,
      columns,
      searchFormSchema,
      pageApi: brLineAuditedPage,
      vxeGridOptions: {
        toolbarConfig: {
          buttons: [
            {
              content: '转采购',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:ballot-recount',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: handleToPurchase,
                },
              },
              visible: hasPermission(auth.toPurchase),
            },
            {
              content: '驳回申请',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:file-undo-outline',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: handleReject,
                },
              },
              visible: hasPermission(auth.reject),
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
                  export: 'erp:BuyRequestDetail:export',
                },
              },
            },
          ],
        },
      },
      module: 'erp',
    });

  function handleReject() {
    const selectedRows = tableRef.value?.getCheckboxRecords();
    if (!selectedRows || selectedRows.length === 0) {
      createMessage.warning('请选择一条记录');
      return;
    }
    rows.value = selectedRows;
    openRejectModal(true, {});
  }

  async function handleToPurchase() {
    const selectedRows = tableRef.value?.getCheckboxRecords();
    if (!selectedRows || selectedRows.length === 0) {
      createMessage.warning('请选择一条记录');
      return;
    }
    const itemCodes: string[] = [];
    selectedRows.forEach((item) => {
      !itemCodes.includes(item.itemCode) && itemCodes.push(item.itemCode);
    });

    const res = await supplyListByItem(itemCodes.join(','));
    openModal(true, {
      lines: selectedRows,
      supplyList: res,
    });
  }

  function checCheckboxkMethod({ row }) {
    return row.lineStatus === '01';
  }
</script>

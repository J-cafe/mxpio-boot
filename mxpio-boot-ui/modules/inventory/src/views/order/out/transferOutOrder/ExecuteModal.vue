<template>
  <BasicModal
    width="1200px"
    :height="600"
    title="调拨出库执行"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    BasicVxeTableProps,
    VxeGridInstance,
  } from '@mxpio/components';
  import { executeColumns } from './transferOutOrder.data';
  import { woExecute, wolineList, useExecuteClassifyData } from '@mxpio/bizcommon';
  import Big from 'big.js';
  import { useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'TransferOutOrderExecuteModal' });
  const emit = defineEmits(['success', 'register']);

  let formData: Recordable = {};
  const dataSource = ref([]);
  const tableRef = ref<VxeGridInstance>();
  const { createMessage } = useMessage();
  const { classifyWhFormData } = useExecuteClassifyData();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'TransferOutOrderExecuteTable',
      keepSource: true,
      minHeight: '200px',
      tableClass: '!px-0 !py-0',
      columns: executeColumns,
      data: dataSource.value,
      proxyConfig: { enabled: false },
      pagerConfig: {
        enabled: false,
      },
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },
      editRules: {
        executeQuantity: [
          { required: true, message: '请输入出库数量', trigger: 'change' },
          {
            validator: ({ cellValue, row }) => {
              if (cellValue <= 0) {
                return new Error('出库数量不能小于等于0');
              }
              if (cellValue > row.maxQuantity) {
                return new Error('不能大于数量' + row.maxQuantity);
              }
            },
            trigger: 'change',
          },
        ],
      },
      rowClassName: ({ row }) => {
        if (Number(row.maxQuantity) > Number(row.liveStockQuantity || 0)) {
          return 'bg-red-200';
        }
      },
    };
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    formData = data.record;
    const res = await wolineList(formData.bizNo);
    dataSource.value = res.map((item) => {
      const maxQuantity = item.actualQuantity
        ? Big(item.quantity)
            .minus(item.actualQuantity)
            .plus(item.toleranceRangeQuantity || 0)
        : Big(item.quantity).plus(item.toleranceRangeQuantity || 0);
      const executeQuantity = item.actualQuantity
        ? Big(item.quantity).minus(item.actualQuantity)
        : Big(item.quantity);
      return {
        ...item,
        executeQuantity: executeQuantity.toNumber(),
        maxQuantity: maxQuantity.toNumber(),
        hasLot: !!item.lotNo,
        whCode: formData.whCode,
      };
    });
    tableRef.value?.loadData(dataSource.value || []);
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectedRows = tableRef.value?.getCheckboxRecords();
      if (!selectedRows || selectedRows.length === 0) {
        createMessage.error('请选择要执行的行');
        return;
      }
      let errMap = await tableRef.value?.validate(selectedRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await woExecute(formData.bizNo, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const selectedRows = tableRef.value?.getCheckboxRecords() || [];
    const executeData = classifyWhFormData(selectedRows);
    return {
      lines: executeData,
    };
  }
</script>

<template>
  <BasicModal
    width="1200px"
    :height="600"
    title="材料出库明细执行"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    BasicVxeTableProps,
    VxeGridInstance,
  } from '@mxpio/components';
  import { executeColumns } from './rawOutDetail.data';
  import { woExecutebatchApi, useExecuteClassifyData } from '@mxpio/bizcommon';
  import Big from 'big.js';

  defineOptions({ name: 'RawOutDetailExecuteModal' });

  const emit = defineEmits(['success', 'register']);
  const dataSource = ref([]);
  const tableRef = ref<VxeGridInstance>();
  const { classifyWhFormData } = useExecuteClassifyData();
  const gridOptions = ref<BasicVxeTableProps>({
    id: 'RawOutDetailExecuteTable',
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
              return new Error('不能大于最大数量' + row.maxQuantity);
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
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    dataSource.value = data.rows.map((item) => {
      const maxQuantity = item.actualQuantity
        ? Big(item.quantity)
            .minus(item.actualQuantity)
            .plus(item.toleranceRangeQuantity || 0)
            .toNumber()
        : Big(item.quantity)
            .plus(item.toleranceRangeQuantity || 0)
            .toNumber();
      const executeQuantity = item.actualQuantity
        ? Big(item.quantity).minus(item.actualQuantity).toNumber()
        : item.quantity;
      return {
        ...item,
        executeQuantity: executeQuantity,
        maxQuantity: maxQuantity,
        hasLot: !!item.lotNo,
      };
    });

    tableRef.value?.loadData(dataSource.value || []);
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await woExecutebatchApi(executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const executeData = classifyWhFormData(fullData);
    return {
      lines: executeData,
    };
  }
</script>

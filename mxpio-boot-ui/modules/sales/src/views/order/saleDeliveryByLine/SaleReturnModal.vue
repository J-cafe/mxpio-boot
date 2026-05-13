<template>
  <BasicModal
    width="1200px"
    title="销售退货"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    BasicVxeTableProps,
    VxeGridInstance,
  } from '@mxpio/components';
  import { returnColumns } from './saleDeliveryByLine.data';
  import { salesOrderReject, useExecuteClassifyData } from '@mxpio/bizcommon';
  // import { useMessage } from '@mxpio/hooks';
  import Big from 'big.js';

  defineOptions({ name: 'SaleReturnModal' });

  const emit = defineEmits(['success', 'register']);

  let bizNo: string = '';
  const dataSource = ref([]);
  const tableRef = ref<VxeGridInstance>();
  // const { createMessage } = useMessage();
  const { classifyOrderFormData } = useExecuteClassifyData();
  /**
   * 表格配置
   */
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'SaleReturnTable',
    keepSource: true,
    minHeight: '200px',
    tableClass: '!px-0 !py-0',
    columns: returnColumns,
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
        { required: true, message: '请输入发货数量', trigger: 'change' },
        {
          validator: ({ cellValue, row }) => {
            if (cellValue <= 0) {
              return new Error('发货数量不能小于等于0');
            }
            if (cellValue > row.maxQuantity) {
              return new Error('不能大于最大发货数量');
            }
          },
          trigger: 'change',
        },
      ],
      whCode: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    },
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    bizNo = data.bizNo;
    dataSource.value = data.record.map((item: Recordable) => {
      const maxQuantity = Big(item.actualQuantity || 0)
        .minus(item.planRejectQuantity || 0)
        .minus(item.actualRejectQuantity || 0)
        .toNumber();
      return {
        lineNo: item.lineNo,
        itemCode: item.itemCode,
        itemName: item.itemName,
        itemSpec: item.itemSpec,
        unitCode: item.unitCode,
        bizNo: item.bizNo,
        quantity: item.quantity,
        maxQuantity: maxQuantity,
        textMap: {
          unitCode$DICT_TEXT_: item.textMap['unitCode$DICT_TEXT_'],
        },
      };
    });
    tableRef.value?.loadData(dataSource.value);
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      let errMap = await tableRef.value?.validate(true);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await salesOrderReject(bizNo, executeData);
      // createMessage.success('操作成功');
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const submitData = classifyOrderFormData(
      fullData,
      (item: Recordable, lot: Recordable | undefined) => {
        return {
          whCode: lot?.whCode,
        };
      },
    );
    return {
      lines: submitData,
    };
  }
</script>

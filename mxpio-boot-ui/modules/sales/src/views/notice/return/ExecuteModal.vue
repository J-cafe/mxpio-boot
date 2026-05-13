<template>
  <BasicModal
    width="1200px"
    title="销售退货确认"
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
  import { executeColumns } from './saleReturn.data';
  import { saleReturnExecute, rbLineList, useExecuteClassifyData } from '@mxpio/bizcommon';

  defineOptions({ name: 'SaleReturnExecuteModal' });

  const emit = defineEmits(['success', 'register']);

  let notice: Recordable = {};
  const dataSource = ref([]);
  const tableRef = ref<VxeGridInstance>();
  const { classifyNoticeFormData } = useExecuteClassifyData();
  const gridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'SaleReturExecuteTable',
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
          { required: true, message: '请输入确认数量', trigger: 'change' },
          {
            validator: ({ cellValue, row }) => {
              if (cellValue <= 0) {
                return new Error('发货数量不能小于等于0');
              }
              if (cellValue > row.quantity) {
                return new Error('不能大于数量' + row.quantity);
              }
            },
            trigger: 'change',
          },
        ],
      },
    };
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    notice = data.record;
    const res = await rbLineList(notice.noticeNo);
    dataSource.value = res.map((item) => {
      return {
        ...item,
        executeQuantity: item.quantity,
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
      await saleReturnExecute(notice.noticeNo, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const { fullData = [] } = tableRef.value?.getTableData() || {};
    const executeData = classifyNoticeFormData(fullData, notice);
    return {
      lines: executeData,
    };
  }
</script>

<template>
  <BasicModal
    width="1200px"
    title="请选择需要退料的批次"
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
  import { returnColumns } from './execute.data';
  import {
    returnEqpRepairApi,
    pickingEqpRepairListApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import { Big } from '@mxpio/utils';

  defineOptions({ name: 'EqpUpkeepReturnModal' });

  const emit = defineEmits(['success', 'register']);

  let bizNo: string = '';
  const dataSource = ref([]);
  const tableRef = ref<VxeGridInstance>();
  const { createMessage } = useMessage();
  const { classifyOrderFormData } = useExecuteClassifyData();
  /**
   * 表格配置
   */
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'EqpUpkeepReturnTable',
    keepSource: true,
    minHeight: 300,
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
        { required: true, message: '请输入退料数量', trigger: 'change' },
        {
          validator: ({ cellValue, row }) => {
            if (cellValue <= 0) {
              return new Error('退料数量不能小于等于0');
            }
            if (cellValue > row.maxQuantity) {
              return new Error('不能大于最大退料数量');
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
    getLines();
  });

  async function getLines() {
    const res = await pickingEqpRepairListApi(bizNo);

    res.forEach((item: Recordable) => {
      const maxQuantity = Big(item.actualQuantity || 0)
        .minus(item.planRejectQuantity || 0)
        .minus(item.actualRejectQuantity || 0)
        .toNumber();
      item.maxQuantity = maxQuantity;
    });

    tableRef.value?.loadData(res);
  }

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectedRows = tableRef.value?.getCheckboxRecords();
      if (!selectedRows || selectedRows.length === 0) {
        createMessage.error('请选择要退料的行');
        return;
      }
      let errMap = await tableRef.value?.validate(selectedRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await returnEqpRepairApi(bizNo, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const selectedRows = tableRef.value?.getCheckboxRecords() || [];
    const submitData = classifyOrderFormData(
      selectedRows,
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

<template>
  <BasicModal
    width="80%"
    title="合格退料"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridEditOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { returnColumns } from './organizeNeedDetail.data';
  import { odRejectApi, useExecuteClassifyData } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import Big from 'big.js';

  const componentName = 'ReturnModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  let bizNo = ref<string>('');
  const tableRef = ref<VxeGridInstance>();
  const dataSource = ref([]);
  const { classifyOrderFormData } = useExecuteClassifyData();
  const { createMessage } = useMessage();

  const gridEditOptions = reactive<BasicVxeTableProps>({
    id: 'ReturnTable',
    keepSource: true,
    minHeight: '300px',
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
    bizNo.value = data.bizNo;
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
        drawingNo: item.drawingNo,
        materialBrand: item.materialBrand,
        materialType: item.materialType,
        maxQuantity: maxQuantity,
        textMap: item.textMap,
      };
    });
    tableRef.value?.loadData(dataSource.value);
  });

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
      await odRejectApi(bizNo.value, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const selectedRows = tableRef.value?.getCheckboxRecords() || [];
    const submitData = classifyOrderFormData(selectedRows);
    return {
      lines: submitData,
    };
  }
</script>

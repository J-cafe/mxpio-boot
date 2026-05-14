<template>
  <BasicModal
    width="80%"
    title="整套领料"
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
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import type { VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';
  import { pickingColumns, wholeFormSchema } from './manufactureExecute.data';
  import {
    computesetListApi,
    manufactureMoPickingApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'ManufactureWholePickingModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  let bizNo = ref<string>('');
  const { classifyOrderFormData } = useExecuteClassifyData();
  const { createMessage } = useMessage();
  const tableRef = ref<VxeGridInstance>();

  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'WorkshopList',
    tableClass: '!px-0 !py-0',
    columns: pickingColumns,
    toolbarConfig: {
      buttons: [],
      tools: [],
    },
    formConfig: {
      enabled: true,
      items: wholeFormSchema,
    },
    minHeight: '300px',
    editConfig: {
      trigger: 'click',
      mode: 'row',
      showStatus: true,
      autoClear: false,
    },
    editRules: {
      executeQuantity: [
        { required: true, message: '请输入领料数量', trigger: 'change' },
        {
          validator: ({ cellValue, row }) => {
            if (cellValue <= 0) {
              return new Error('领料数量不能小于等于0');
            }
            if (cellValue > row.maxQuantity) {
              return new Error('不能大于最大领料数量');
            }
          },
          trigger: 'change',
        },
      ],
      whCode: [{ required: true, message: '请选择仓库', trigger: 'change' }],
    },
    proxyConfig: {
      ajax: {
        query: async ({ form }) => {
          return loadData({ form });
        },
      },
    },
    pagerConfig: {
      enabled: false,
    },
  });

  async function loadData({ form }) {
    const res = await computesetListApi(bizNo.value, form.executeQuantity || '0');
    res.forEach((item: Recordable) => {
      const maxQuantity =
        Number(item.quantity) -
        Number(item.actualQuantity) -
        Number(item.planQuantity) +
        Number(item.actualRejectQuantity);
      item.maxQuantity = maxQuantity;
      item.appointWh = !!item.whCode;
      item.appointLotNo = !!item.lotNo;
      item.executeQuantity = item.setQuantity;
    });
    return res;
  }

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    bizNo.value = data.bizNo;
    tableRef.value?.commitProxy('query');
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectedRows = tableRef.value?.getCheckboxRecords();
      if (!selectedRows || selectedRows.length === 0) {
        createMessage.error('请选择要领料的行');
        return;
      }
      let errMap = await tableRef.value?.validate(selectedRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await manufactureMoPickingApi('41', bizNo.value, executeData);
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

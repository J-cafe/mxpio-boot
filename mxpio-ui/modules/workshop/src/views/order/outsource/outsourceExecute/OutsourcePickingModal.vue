<template>
  <BasicModal
    width="80%"
    title="委外订单领料"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" :tableClass="tableClass" v-bind="gridEditOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable, BasicVxeTableProps } from '@mxpio/components';
  import { pickingColumns, bomFormSchema } from './outsourceExecute.data';
  import { useListCrudHook } from '@mxpio/common';
  import {
    outsourceLineListApi,
    outsourcePickingApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import type { RequestCriteriaParams } from '@mxpio/types';
  import Big from 'big.js';

  const componentName = 'OutsourcePickingModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  let bizNo = ref<string>('');
  const tableClass = '!px-0 !py-0';
  const { classifyOrderFormData } = useExecuteClassifyData();
  const { createMessage } = useMessage();

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    module: 'erp',
    columns: pickingColumns,
    searchFormSchema: bomFormSchema,
    pageApi: async (params?: RequestCriteriaParams) => {
      const dataSource = await outsourceLineListApi(params);
      dataSource.forEach((item: Recordable) => {
        const maxQuantity = Big(item.quantity)
          .minus(item.actualQuantity)
          .minus(item.planQuantity)
          .plus(item.actualRejectQuantity)
          .toNumber();
        item.maxQuantity = maxQuantity;
        item.appointWh = !!item.whCode;
        item.appointLotNo = !!item.lotNo;
        item.executeQuantity = '';
      });
      return dataSource;
    },
    vxeGridOptions: {
      toolbarConfig: {
        buttons: [],
        tools: [],
      },
      pagerConfig: {
        enabled: false,
      },
      proxyConfig: {
        autoLoad: false,
      },
    },
    filters: () => ({ 'bizNo@EQ': bizNo.value }),
  });

  const gridEditOptions = reactive<BasicVxeTableProps>({
    ...gridOptions.value,
    keepSource: true,
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
  });

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
      await outsourcePickingApi('45', bizNo.value, executeData);
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

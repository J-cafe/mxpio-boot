<template>
  <BasicModal
    width="80%"
    title="生产订单补料"
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
  import { BasicModal, useModalInner, VxeBasicTable, BasicVxeTableProps } from '@mxpio/components';
  import { supplementColumns, bomFormSchema } from './manufactureExecute.data';
  import { useListCrudHook } from '@mxpio/common';
  import {
    manufactureLineListApi,
    manufactureMoPickingApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import type { RequestCriteriaParams } from '@mxpio/types';

  defineOptions({ name: 'ManufactureSupplementModal' });

  const emit = defineEmits(['success', 'register']);

  let bizNo = ref<string>('');
  const { classifyOrderFormData } = useExecuteClassifyData();
  const { createMessage } = useMessage();

  const componentName = 'ManufactureSupplementModal';
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    module: 'erp',
    columns: supplementColumns,
    searchFormSchema: bomFormSchema,
    pageApi: async (params?: RequestCriteriaParams) => {
      const dataSource = await manufactureLineListApi(params);
      dataSource.forEach((item: Recordable) => {
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
    tableClass: '!px-0 !py-0',
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
        { required: true, message: '请输入补料数量', trigger: 'change' },
        {
          validator: ({ cellValue }) => {
            if (cellValue <= 0) {
              return new Error('补数量不能小于等于0');
            }
          },
          trigger: 'change',
        },
      ],
      supplementReason: [{ required: true, message: '请输入补料原因', trigger: 'change' }],
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
        createMessage.error('请选择要补料的行');
        return;
      }
      let errMap = await tableRef.value?.validate(selectedRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await manufactureMoPickingApi('42', bizNo.value, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const selectedRows = tableRef.value?.getCheckboxRecords() || [];
    const submitData = classifyOrderFormData(selectedRows, (row: Recordable) => {
      return {
        supplementReason: row.supplementReason,
      };
    });
    return {
      lines: submitData,
    };
  }
</script>

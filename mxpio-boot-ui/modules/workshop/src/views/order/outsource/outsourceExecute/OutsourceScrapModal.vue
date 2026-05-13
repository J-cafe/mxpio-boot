<template>
  <BasicModal
    width="80%"
    title="不合格退料"
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
  import { scrapColumns, bomFormSchema } from './outsourceExecute.data';
  import { useListCrudHook } from '@mxpio/common';
  import {
    outsourceLineListApi,
    executeScrapOutsourceApi,
    useExecuteClassifyData,
  } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import Big from 'big.js';
  import type { RequestCriteriaParams } from '@mxpio/types';

  const componentName = 'OutsourceScrapModal';
  defineOptions({ name: componentName });

  const emit = defineEmits(['success', 'register']);

  let bizNo = ref<string>('');
  const { classifyOrderFormData } = useExecuteClassifyData();
  const { createMessage } = useMessage();

  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    module: 'erp',
    columns: scrapColumns,
    searchFormSchema: bomFormSchema,
    pageApi: async (params?: RequestCriteriaParams) => {
      const dataSource = await outsourceLineListApi(params);
      dataSource.forEach((item: Recordable) => {
        const maxQuantity = Big(item.actualQuantity)
          .minus(item.actualRejectQuantity)
          .minus(item.planRejectQuantity);
        item.maxQuantity = maxQuantity;
        item.whCode = '';
        item.executeQuantity = '';
        item.lotNo = '';
        item.subLotIndex = '';
        item.supplementFlag = '1';
        item.textMap.supplementFlag$DICT_TEXT_ = '是';
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
        { required: true, message: '请输入退料数量', trigger: 'manual' },
        {
          validator: ({ cellValue, row }) => {
            if (cellValue <= 0) {
              return new Error('退料数量不能小于等于0');
            }
            if (cellValue > row.maxQuantity) {
              return new Error('不能大于最大退料数量');
            }
          },
          trigger: 'manual',
        },
      ],
      whCode: [{ required: true, message: '请选择退料接收仓库', trigger: 'manual' }],
      supplementFlag: [{ required: true, message: '请选择是否补料', trigger: 'manual' }],
      defectiveReason: [{ required: true, message: '请输入异常原因', trigger: 'manual' }],
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
        createMessage.error('请选择要退料的行');
        return;
      }
      let errMap = await tableRef.value?.validate(selectedRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData();
      await executeScrapOutsourceApi(bizNo.value, executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData() {
    const selectedRows = tableRef.value?.getCheckboxRecords() || [];
    const submitData = classifyOrderFormData(selectedRows, (item: Recordable, lot) => {
      return {
        supplementFlag: item.supplementFlag,
        whCode: item.whCode,
        reporter: item.reporter,
        reportProc: item.reportProc,
        defectiveReason: item.defectiveReason,
        bomWhCode: lot?.whCode,
      };
    });
    return {
      lines: submitData,
    };
  }
</script>

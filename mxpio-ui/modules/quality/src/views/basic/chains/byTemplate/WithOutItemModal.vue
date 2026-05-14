<template>
  <BasicModal
    width="1200px"
    title="未关联物料"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
  >
    <VxeBasicTable ref="tableRef" v-bind="gridOptions" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import { itemColumns, searchFormItemSchema } from './byTemplate.data';
  import { qtChainItemWithoutApi, saveQtChainApi } from '@mxpio/bizcommon';
  import { useMessage } from '@mxpio/hooks';
  import { useListCrudHook } from '@mxpio/common';

  const componentName = 'WithOutItemModal';
  defineOptions({ name: 'WithOutItemModal' });

  const emit = defineEmits(['success', 'register']);

  let code: string = '';
  let busiType: string = '';
  const { createMessage } = useMessage();
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    columns: itemColumns,
    searchFormSchema: searchFormItemSchema,
    pageApi: (params) => {
      return qtChainItemWithoutApi(busiType, params);
    },
    vxeGridOptions: {
      rowConfig: {
        keyField: 'itemCode',
      },
      toolbarConfig: {
        buttons: [],
        tools: [],
        export: false,
        import: false,
      },
      tableClass: '!px-0 !py-0',
      editConfig: {
        trigger: 'click',
        mode: 'row',
        showStatus: true,
        autoClear: false,
      },
      proxyConfig: {
        autoLoad: false,
      },
    },
    module: 'erp',
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    code = data.code;
    busiType = data.busiType;
    tableRef.value?.commitProxy('query');
  });

  async function handleSubmit() {
    try {
      setModalProps({ confirmLoading: true });
      const selectionRows = tableRef.value?.getCheckboxRecords() || [];
      if (selectionRows.length <= 0) {
        createMessage.error('请选择要关联的物料');
        return;
      }
      let errMap = await tableRef.value?.validate(selectionRows);
      if (errMap) {
        return;
      }
      const executeData = classifyIntoFormData(selectionRows);
      await saveQtChainApi(executeData);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function classifyIntoFormData(selectionRows: Recordable[]) {
    const data: Recordable[] = [];
    selectionRows.forEach((item) => {
      data.push({
        schemeCode: code,
        itemCode: item.itemCode,
        dataScope: '1',
        busiType: busiType,
      });
    });
    return data;
  }
</script>

<template>
  <BasicModal
    width="80%"
    title="版次记录"
    cancelText="关闭"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :showOkBtn="false"
  >
    <VxeBasicTable ref="tableRef" :tableClass="tableClass" v-bind="gridOptions">
      <template #fileName="{ row }">
        <a @click="handleDownload(row)">{{ row.fileName }}</a>
      </template>
    </VxeBasicTable>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable } from '@mxpio/components';
  import { editionColumns } from './procDrawingfile.data';
  import { useListCrudHook } from '@mxpio/common';
  import { drawingfileList } from '@mxpio/bizcommon';
  import { useCommon } from '@mxpio/hooks';

  defineOptions({ name: 'ProcDesignFileEditionModal' });

  // const emit = defineEmits(['success', 'register']);
  const tableClass = '!px-0 !py-0';
  const filters = ref({
    'family@EQ': '',
  });
  const { downloadByFileNo } = useCommon();
  const componentName = 'ProcDesignFileEditionModal';
  const { tableRef, gridOptions } = useListCrudHook({
    componentName,
    module: 'erp',
    columns: editionColumns,
    pageApi: drawingfileList,
    vxeGridOptions: {
      height: 300,
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
      sortConfig: {
        defaultSort: {
          field: 'edition',
          order: 'desc',
        },
      },
    },
    filters: filters.value,
  });

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    filters.value['family@EQ'] = data.family;
    tableRef.value?.commitProxy('query');
  });

  async function handleDownload(row) {
    try {
      await downloadByFileNo(row.fileNo);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }
</script>

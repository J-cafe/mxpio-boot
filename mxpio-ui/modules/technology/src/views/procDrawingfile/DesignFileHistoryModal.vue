<template>
  <BasicModal
    width="1000px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="工艺图纸"
    cancelText="关闭"
    :showOkBtn="false"
  >
    <VxeBasicTable
      class="pt-0"
      ref="procBomTableRef"
      v-bind="procBomGridOptions"
      @current-change="onCurrentChange"
      @data-change="onDataChange"
    />
    <VxeBasicTable class="pt-0" ref="designFileTableRef" v-bind="designFileGridOptions">
      <template #fileName="{ row }">
        <a @click="handleDownload(row)">{{ row.fileName }}</a>
      </template>
      <template #edition="{ row }">
        <a @click="openEditionModal(true, row)">{{ row.edition }}</a>
      </template>
    </VxeBasicTable>
  </BasicModal>
  <ProcDesignFileEditionModal width="900px" @register="registerEditionModal" />
</template>
<script lang="ts" setup>
  import { ref, computed } from 'vue';
  import { BasicModal, useModalInner, VxeBasicTable, useModal } from '@mxpio/components';
  import { procLineList, drawingfileListByProc } from '@mxpio/bizcommon';
  import type { BasicVxeTableProps } from '@mxpio/components';
  import { procBomDetailColumns, procDrawingfileColumns } from './procDrawingfile.data';
  import { useCommon } from '@mxpio/hooks';
  import ProcDesignFileEditionModal from './ProcDesignFileEditionModal.vue';

  defineOptions({ name: 'DesignFileHistoryModal' });

  const designFileTableRef = ref();
  const procBomTableRef = ref();
  const prodrout = ref<Recordable>({});
  const rout = ref<Recordable>({});
  const { downloadByFileNo } = useCommon();
  const [registerEditionModal, { openModal: openEditionModal }] = useModal();
  const procBomGridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'ProcBomHistoryTable',
      keepSource: true,
      height: 300,
      columns: procBomDetailColumns,
      proxyConfig: {
        ajax: {
          query: async () => {
            return loadData();
          },
        },
        autoLoad: false,
      },
      radioConfig: {
        trigger: 'row',
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  const designFileGridOptions = computed<BasicVxeTableProps>(() => {
    return {
      id: 'DesignFileHistoryTable',
      keepSource: true,
      height: 300,
      columns: procDrawingfileColumns.slice(0, -1), // 删除最后一列操作列
      proxyConfig: {
        ajax: {
          query: async () => {
            return drawingfileListByProc(prodrout.value?.id || '', rout.value?.version || '');
          },
        },
        autoLoad: false,
      },
      radioConfig: {
        trigger: 'row',
      },
      pagerConfig: {
        enabled: false,
      },
    };
  });

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    rout.value = data.record;
    procBomTableRef.value?.commitProxy('query');
  });

  async function loadData() {
    try {
      if (!rout.value?.routId) {
        return [];
      }
      const res = await procLineList(rout.value?.routId);
      return res;
    } catch (error) {
      console.error('获取BOM列表失败', error);
    }
  }

  function onCurrentChange({ row }) {
    prodrout.value = row;
    if (!row?.id) {
      return;
    }
    designFileTableRef.value?.commitProxy('query');
  }

  function onDataChange({ visibleData }) {
    procBomTableRef.value?.setCurrentRow(visibleData[0]);
    procBomTableRef.value?.setRadioRow(visibleData[0]);
    prodrout.value = visibleData[0];
    if (!prodrout.value?.id) {
      return;
    }
    designFileTableRef.value?.commitProxy('query');
  }

  async function handleDownload(row) {
    try {
      await downloadByFileNo(row.fileNo);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }
</script>

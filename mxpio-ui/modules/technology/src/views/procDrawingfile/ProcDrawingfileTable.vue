<template>
  <VxeBasicTable ref="tableRef" v-bind="gridOptions">
    <template #action="{ row }">
      <TableAction
        :outside="true"
        :actions="[
          {
            label: '签出',
            onClick: signOut.bind(null, row),
            ifShow: row.signOffFlag === '0',
            auth: auth.edit,
          },
          {
            label: '签入',
            onClick: signIn.bind(null, row),
            ifShow: row.signOffFlag === '1' && username === row.signOffer,
            auth: auth.edit,
          },
        ]"
        :dropDownActions="[
          {
            label: '取消',
            color: 'error',
            popConfirm: {
              title: '是否确认取消',
              placement: 'left',
              confirm: handleCancel.bind(null, row),
            },
            auth: auth.delete,
            ifShow: row.signOffFlag === '1',
          },
          {
            label: '删除',
            color: 'error',
            popConfirm: {
              title: '是否确认删除',
              placement: 'left',
              confirm: handleDelete.bind(null, row),
            },
            ifShow: row.signOffFlag === '0' && row.version === 1 && row.edition === 1,
            auth: auth.delete,
          },
        ]"
      />
    </template>
    <template #fileName="{ row }">
      <a @click="handleDownload(row)">{{ row.fileName }}</a>
    </template>
    <template #edition="{ row }">
      <a @click="openEditionModal(true, row)">{{ row.edition }}</a>
    </template>
  </VxeBasicTable>
  <ProcDrawingfileModal
    width="900px"
    :prodrout="prodrout"
    :rout="rout"
    @register="registerModal"
    @success="handleSuccess"
  />
  <ProcDesignFileEditionModal width="900px" @register="registerEditionModal" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction, useModal } from '@mxpio/components';
  import { procDrawingfileColumns } from './procDrawingfile.data';
  import {
    drawingfileListByProc,
    deleteDrawingfile,
    signOutDrawingfile,
    signOutCancleDrawingfile,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import ProcDrawingfileModal from './ProcDrawingfileModal.vue';
  import ProcDesignFileEditionModal from './ProcDesignFileEditionModal.vue';
  import { useCommon, useMessage } from '@mxpio/hooks';
  import { useUserStore } from '@mxpio/stores';
  import { useDebounceFn } from '@vueuse/core';

  const componentName = 'ProcDrawingfileTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    prodrout: {
      type: Object,
      default: () => ({}),
    },
    rout: {
      type: Object,
      default: () => ({}),
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => [props.prodrout],
    () => {
      debounceLoadData();
    },
    {
      deep: true,
    },
  );
  const { downloadByFileNo } = useCommon();
  const { createMessage } = useMessage();
  const userStore = useUserStore();
  const [registerEditionModal, { openModal: openEditionModal }] = useModal();
  const { username } = userStore.getUserInfo || {};
  const { tableRef, auth, gridOptions, registerModal, openModal, handleDelete, handleSuccess } =
    useListCrudHook({
      componentName,
      columns: procDrawingfileColumns,
      pageApi: () => drawingfileListByProc(props.prodrout?.id || '', props.rout?.version || ''),
      deleteApi: deleteDrawingfile,
      vxeGridOptions: {
        tableClass: '!px-0 !py-0',
        height: '300px',
        rowConfig: {
          keyField: 'id',
        },
        toolbarConfig: {
          import: false,
        },
        pagerConfig: {
          enabled: false,
        },
        proxyConfig: {
          enabled: false,
        },
      },
      module: 'erp',
    });

  async function loadData() {
    try {
      if (props.rout.version && props.prodrout.id) {
        tableRef.value?.commitProxy('query');
      } else {
        tableRef.value?.reloadData([]);
      }
    } catch (error) {
      console.error('Error loading data:', error);
    }
  }

  async function handleDownload(row) {
    try {
      await downloadByFileNo(row.fileNo);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }

  async function signIn(row) {
    try {
      openModal(true, {
        record: row,
        isUpdate: true,
      });
    } catch (error) {
      console.error('Error signing in:', error);
    }
  }

  async function signOut(row) {
    try {
      await signOutDrawingfile(row.id);
      createMessage.success('签出成功');
      handleSuccess();
    } catch (error) {
      console.error('Error signing out:', error);
    }
  }

  async function handleCancel(row) {
    try {
      await signOutCancleDrawingfile(row.id);
      createMessage.success('取消成功');
      handleSuccess();
    } catch (error) {
      console.error('Error canceling:', error);
    }
  }
</script>

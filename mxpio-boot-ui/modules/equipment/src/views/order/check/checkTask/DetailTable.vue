<template>
  <VxeBasicTable
    ref="tableRef"
    v-bind="gridOptions"
    @checkbox-change="checkboxChange"
    @checkbox-all="checkboxChange"
  >
    <template #action="{ row }">
      <TableAction
        :outside="true"
        :actions="[
          {
            label: '编辑',
            onClick: handleEdit.bind(null, row),
            auth: auth.edit,
            ifShow: checkTask.orderStatus === '10',
          },
        ]"
        :dropDownActions="[
          {
            label: '选项',
            onClick: openParam.bind(null, row),
            ifShow: row.chooseEnable === 1,
          },
          {
            label: '删除',
            color: 'error',
            popConfirm: {
              title: '是否确认删除',
              placement: 'left',
              confirm: handleDelete.bind(null, row),
            },
            auth: auth.delete,
            ifShow: checkTask.orderStatus === '10',
          },
        ]"
      />
    </template>
    <template #pic="{ row }">
      <a v-if="row.pic" @click="handleDownload(row)">下载</a>
    </template>
  </VxeBasicTable>
  <DetailModal
    width="80%"
    @register="registerModal"
    @success="handleSuccess"
    :bizNo="props.bizNo"
  />
  <DetailParamModal width="900px" @register="registerParamModal" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction, useModal } from '@mxpio/components';
  import { detailColumns } from './eqpCheckTask.data';
  import { eqpCheckTaskDetailPageApi, deleteEqpCheckTaskDetailApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useCommon, useMessage } from '@mxpio/hooks';
  import { useDebounceFn } from '@vueuse/core';
  import DetailModal from './DetailModal.vue';
  import DetailParamModal from './DetailParamModal.vue';

  const componentName = 'DetailTable';
  defineOptions({ name: componentName });

  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
    checkTask: {
      type: Object,
      default: () => {},
    },
  });
  const { createMessage } = useMessage();
  const [registerParamModal, { openModal: openParamModal }] = useModal();
  const debounceLoadData = useDebounceFn(loadData, 200);
  watch(
    () => [props.bizNo, props.checkTask.orderStatus],
    () => {
      debounceLoadData();
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const { downloadByFileNo } = useCommon();
  const {
    tableRef,
    auth,
    gridOptions,
    handleEdit,
    handleDelete,
    registerModal,
    handleSuccess,
    checkboxChange,
  } = useListCrudHook({
    componentName,
    columns: detailColumns,
    pageApi: eqpCheckTaskDetailPageApi,
    deleteApi: deleteEqpCheckTaskDetailApi,
    vxeGridOptions: {
      tableClass: '!px-0 !py-0',
      height: 280,
      rowConfig: {
        keyField: 'id',
      },
      toolbarConfig: {
        import: false,
      },
      proxyConfig: {
        enabled: false,
      },
    },
    filters: () => ({
      'bizNo@EQ': props.bizNo,
    }),
    module: 'erp',
    addBefore,
    deleteBefore: addBefore,
  });

  async function loadData() {
    try {
      if (props.bizNo) {
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
      await downloadByFileNo(row.pic);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }

  async function addBefore() {
    try {
      if (!props.bizNo) {
        createMessage.warning('请选择主记录！');
        return Promise.reject();
      }
      if (props.checkTask.orderStatus !== '10') {
        createMessage.warning('当前状态不可操作');
        return Promise.reject();
      }
    } catch (error) {
      console.error('Error adding by category:', error);
    }
  }

  function openParam(row) {
    openParamModal(true, { record: row });
  }
</script>

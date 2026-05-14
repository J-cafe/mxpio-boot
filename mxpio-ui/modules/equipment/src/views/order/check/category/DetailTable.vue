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
          },
        ]"
      />
    </template>
    <template #attrUrl="{ row }">
      <a v-if="row.attrUrl" @click="handleDownload(row)">{{ row.textMap['attrUrl$DICT_TEXT_'] }}</a>
    </template>
  </VxeBasicTable>
  <DetailModal
    width="80%"
    @register="registerModal"
    @success="handleSuccess"
    :categoryId="props.categoryId"
  />
  <DetailParamModal @register="registerParamModal" />
</template>
<script lang="ts" setup>
  import { watch } from 'vue';
  import { VxeBasicTable, TableAction, useModal } from '@mxpio/components';
  import { detailColumns } from './eqpCheckCategory.data';
  import { eqpCheckCategoryDetailPageApi, deleteEqpCheckCategoryDetailApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useCommon } from '@mxpio/hooks';
  import { useDebounceFn } from '@vueuse/core';
  import DetailModal from './DetailModal.vue';
  import DetailParamModal from './DetailParamModal.vue';

  const componentName = 'DetailModal';
  defineOptions({ name: componentName });

  const props = defineProps({
    categoryId: {
      type: String,
      default: () => '',
    },
  });

  const debounceLoadData = useDebounceFn(loadData, 200);
  const [registerParamModal, { openModal: openParamModal }] = useModal();
  watch(
    () => props.categoryId,
    () => {
      debounceLoadData();
    },
    {
      deep: true,
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
    pageApi: eqpCheckCategoryDetailPageApi,
    deleteApi: deleteEqpCheckCategoryDetailApi,
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
      'categoryId@EQ': props.categoryId,
    }),
    module: 'erp',
  });

  async function loadData() {
    try {
      if (props.categoryId) {
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
      await downloadByFileNo(row.attrUrl);
    } catch (error) {
      console.error('Error downloading file:', error);
    }
  }

  function openParam(row) {
    openParamModal(true, { record: row });
  }
</script>

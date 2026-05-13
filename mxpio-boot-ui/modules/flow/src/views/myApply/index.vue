<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '查看',
              onClick: handleView.bind(null, row),
            },
            {
              label: '暂停',
              onClick: handleSuspend.bind(null, row),
              ifShow: row.state === 'ACTIVE',
            },
            {
              label: '开始',
              onClick: handleStart.bind(null, row),
              ifShow: row.state === 'SUSPENDED',
            },
            {
              label: '加急',
              onClick: handleUrgent.bind(null, row),
              ifShow: row.state === 'ACTIVE',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <BPMNTaskModal
      :width="1200"
      type="view"
      :hasCancel="hasCancel"
      @register="registerModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import type { VxeGridPropTypes, VxeGridInstance, BasicVxeTableProps } from '@mxpio/components';

  import { reactive, ref } from 'vue';
  import { myApplyList, suspend, start, urgent } from '@mxpio/bizcommon';
  import { columns, searchFormSchema } from './myApply.data';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';

  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import BPMNTaskModal from '../task/BPMNTaskModal.vue';
  import { useProfile, useMessage } from '@mxpio/hooks';

  defineOptions({ name: 'MyApply' });

  const [registerModal, { openModal }] = useModal();
  const { createMessage } = useMessage();
  const hasCancel = ref(false);

  const { restoreStore, updateStore } = useProfile();
  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'MyApply',
    columns: columns,
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    minHeight: 700,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
    },
    customConfig: {
      storage: {
        visible: true,
        resizable: true,
        sort: true,
        fixed: true,
      }, // 启用自定义列状态保存功能
      restoreStore: restoreStore,
      updateStore: updateStore,
    },
  });

  function loadData(queryParams: VxeTableQueryParams) {
    const params = getVxeTableQueryParams(queryParams);
    return myApplyList(params);
  }

  function handleView(record: Recordable) {
    hasCancel.value = record.state === 'ACTIVE';
    openModal(true, {
      isUpdate: false,
      record: {
        ...record,
        processInstanceId: record.id,
      },
    });
  }

  function handleSuccess() {
    tableRef.value?.commitProxy('query');
  }

  async function handleSuspend(row: Recordable) {
    try {
      await suspend(row.id);
      createMessage.success('暂停成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleStart(row: Recordable) {
    try {
      await start(row.id);
      createMessage.success('开始成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  function handleUrgent(row: Recordable) {
    urgent(row.id, { $bpmnSortFlag: '1' }).then((res) => {
      if (res.success) {
        handleSuccess();
      }
    });
  }
</script>

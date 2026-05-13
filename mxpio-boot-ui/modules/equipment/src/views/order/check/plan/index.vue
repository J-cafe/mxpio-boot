<template>
  <div class="m-3 bg-white">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @current-change="onCurrentChange"
      @data-change="onDataChange"
    >
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: auth.edit,
              ifShow: row.isEnable === 0 && row.execStatus === '1',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '生效',
              onClick: handleEnable.bind(null, row),
              ifShow: row.isEnable === 0 && row.execStatus === '1',
            },
            {
              label: '恢复',
              onClick: handleRecover.bind(null, row),
              ifShow: row.isEnable === 1 && row.pauseGenerateTask === 1,
            },
            {
              label: '暂停',
              onClick: handlePause.bind(null, row),
              ifShow: row.isEnable === 1 && row.pauseGenerateTask !== 1,
            },
            {
              label: '作废',
              onClick: handleDisable.bind(null, row),
              ifShow: row.isEnable === 1,
            },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              ifShow: row.isEnable === 0 && row.execStatus === '1',
              auth: auth.delete,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="点巡检计划目标">
        <TargetTable :planId="currentRow.id" :plan="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="点巡检计划明细">
        <DetailTable :planId="currentRow.id" :plan="currentRow" />
      </a-tab-pane>
    </a-tabs>
    <EqpCheckCategoryModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    deleteEqpCheckPlanApi,
    eqpCheckPlanPageApi,
    enableEqpCheckPlanApi,
    recoverEqpCheckPlanApi,
    pauseEqpCheckPlanApi,
    disableEqpCheckPlanApi,
    copyEqpCheckPlanApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './eqpCheckPlan.data';
  import EqpCheckCategoryModal from './EqpCheckPlanModal.vue';
  import DetailTable from './DetailTable.vue';
  import TargetTable from './TargetTable.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'EqpCheckPlanList';
  defineOptions({ name: componentName });

  const currentRow = ref<Recordable>({});
  const { createMessage } = useMessage();
  const {
    tableRef,
    auth,
    gridOptions,
    registerModal,
    handleEdit,
    handleDelete,
    handleDetail,
    handleSuccess,
  } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: eqpCheckPlanPageApi,
    deleteApi: deleteEqpCheckPlanApi,
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0',
      rowConfig: {
        keyField: 'id',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    appendButtons: [
      {
        content: '复制',
        buttonRender: {
          name: 'AButton',
          props: {
            type: 'primary',
            preIcon: 'mdi:content-copy',
          },
          attrs: {
            class: 'ml-2',
          },
          events: {
            click: () => handleCopy(),
          },
        },
      },
    ],
    module: 'erp',
  });

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0]);
    tableRef.value?.setRadioRow(visibleData[0]);
  }

  async function handleEnable(row) {
    try {
      await enableEqpCheckPlanApi(row.id);
      createMessage.success('操作成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleRecover(row) {
    try {
      await recoverEqpCheckPlanApi(row.id);
      createMessage.success('操作成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handlePause(row) {
    try {
      await pauseEqpCheckPlanApi(row.id);
      createMessage.success('操作成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleDisable(row) {
    try {
      await disableEqpCheckPlanApi(row.id);
      createMessage.success('操作成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleCopy() {
    try {
      if (!currentRow.value.id) {
        createMessage.warning('请选择主记录！');
        return Promise.reject();
      }
      await copyEqpCheckPlanApi(currentRow.value.id);
      createMessage.success('操作成功');
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }
</script>

<template>
  <div class="m-3 bg-white">
    <a-tabs
      class="px-6"
      v-model:activeKey="activeKey"
      :tabBarStyle="{ marginBottom: 0 }"
      @change="tabChange"
    >
      <a-tab-pane key="pending" tab="待处理" />
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane key="seven" tab="近七日" />
    </a-tabs>
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
              ifShow: row.orderStatus === '10',
            },
          ]"
          :dropDownActions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
            {
              label: '下达',
              onClick: handleRelease.bind(null, row),
              auth: authConfig.release,
              ifShow: row.orderStatus === '10',
            },
            // {
            //   label: '变更时间',
            //   onClick: handleChangeTime.bind(null, row),
            //   auth: authConfig.release,
            //   ifShow: row.orderStatus === '10',
            // },
            {
              label: '删除',
              color: 'error',
              popConfirm: {
                title: '是否确认删除',
                placement: 'left',
                confirm: handleDelete.bind(null, row),
              },
              auth: auth.delete,
              ifShow: row.orderStatus === '10',
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="6" tab="保养任务明细">
        <DetailTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="1" tab="操作历史">
        <RecordTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="8" tab="重要备注">
        <RemarkTable :bizNo="currentRow.bizNo" />
      </a-tab-pane>
    </a-tabs>
    <UpkeepTaskModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    eqpUpkeepTaskPageApi,
    deleteEqpUpkeepTaskApi,
    resConfig,
    releaseEqpUpkeepApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './upkeepTask.data';
  import UpkeepTaskModal from './UpkeepTaskModal.vue';
  import DetailTable from './DetailTable.vue';
  import RecordTable from './RecordTable.vue';
  import RemarkTable from '../execute/RemarkTable.vue';

  const componentName = 'EqpUpkeepTaskList';
  defineOptions({ name: componentName });

  const authConfig = {
    release: `erp:${componentName}:release`,
    reject: `erp:${componentName}:reject`,
  };

  const currentRow = ref<Recordable>({});
  // const [registerRejectModal, { openModal: openRejectModal }] = useModal();
  // const { createMessage } = useMessage();
  // const { hasPermission } = usePermission();
  const closeStatus = ref(['1']);
  const activeKey = ref('pending');
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
    pageApi: eqpUpkeepTaskPageApi,
    deleteApi: deleteEqpUpkeepTaskApi,
    configApi: () => resConfig('WMO1'),
    vxeGridOptions: {
      height: 450,
      tableClass: 'pb-0 !py-0',
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
    },
    module: 'erp',
    appendButtons: [
      // {
      //   content: '占用',
      //   buttonRender: {
      //     name: 'AButton',
      //     props: {
      //       type: 'primary',
      //       preIcon: 'mdi:clock-start',
      //     },
      //     attrs: {
      //       class: 'ml-2',
      //     },
      //     events: {
      //       click: () => handleReject(),
      //     },
      //   },
      //   visible: hasPermission(authConfig.reject),
      // },
      {
        buttonRender: {
          name: 'ACheckboxGroup',
          props: () => ({
            options: [
              {
                label: '过滤已否决',
                value: '1',
              },
            ],
            value: closeStatus, // 直接传 ref，自动双向绑定
          }),
          attrs: {
            class: 'ml-2',
          },
          events: {
            change: () => {
              tableRef.value?.commitProxy('query');
            },
          },
        },
      },
    ],
    filters: () => {
      return {
        'orderStatus@IN': closeStatus.value.length > 0 ? '10' : '10,90',
        'nextFlag@EQ': activeKey.value === 'pending' ? '1' : undefined,
        sevenDaysPastQuery: activeKey.value === 'seven' ? true : undefined,
      };
    },
  });

  // 同步更新设备
  const handleRelease = async (row: Recordable) => {
    try {
      await releaseEqpUpkeepApi(row.bizNo);
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  };

  function tabChange() {
    tableRef.value?.commitProxy('query');
  }

  function onCurrentChange({ row }) {
    currentRow.value = row;
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setCurrentRow(visibleData[0] || {});
    tableRef.value?.setRadioRow(visibleData[0] || {});
  }

  // function handleReject() {
  //   if (!currentRow.value.bizNo) {
  //     createMessage.error('请选择点检任务');
  //     return;
  //   }
  //   openRejectModal(true, {
  //     bizNo: currentRow.value.bizNo,
  //   });
  // }
</script>

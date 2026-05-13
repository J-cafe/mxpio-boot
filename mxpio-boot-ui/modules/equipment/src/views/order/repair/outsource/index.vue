<template>
  <div class="m-3 bg-white">
    <VxeBasicTable
      ref="tableRef"
      v-bind="gridOptions"
      @data-change="onDataChange"
      @current-change="onCurrentChange"
    >
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '详情',
              onClick: handleDetail.bind(null, row),
            },
          ]"
          :dropDownActions="[
            {
              label: '下达',
              onClick: handleIssusd.bind(null, row),
              auth: authConfig.issusd,
              ifShow: row.orderStatus === '10',
            },
            {
              label: '否决',
              onClick: handleReject.bind(null, row),
              auth: authConfig.reject,
              ifShow: row.orderStatus === '10',
            },
          ]"
        />
      </template>
      <template #bpmnSortFlag="{ row }">
        <a-tag v-if="row.bpmnSortFlag === '0'" color="#2db7f5">普通</a-tag>
        <a-tag v-else color="red">紧急</a-tag>
      </template>
    </VxeBasicTable>
    <a-tabs class="px-6">
      <a-tab-pane key="1" tab="询价明细">
        <InquiryTable @ok="handleSuccess" :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
      <a-tab-pane key="2" tab="重要备注">
        <RemarkTable :bizNo="currentRow.bizNo" :task="currentRow" />
      </a-tab-pane>
    </a-tabs>
    <RepairTaskModal width="80%" @register="registerModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { Tag as ATag } from 'ant-design-vue';
  import { TableAction, VxeBasicTable } from '@mxpio/components';
  import {
    repairOutsourcePageApi,
    repairOutsourceIssuedApi,
    repairOutsourceRejectApi,
    repairOutsourceInquiryEndApi,
    getRepairByBizNoApi,
  } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { useMessage, usePermission } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './outsource.data';
  import InquiryTable from './InquiryTable.vue';
  import RemarkTable from './RemarkTable.vue';
  import RepairTaskModal from '../task/RepairTaskModal.vue';

  const componentName = 'RepairOutsourceList';
  defineOptions({ name: componentName });
  const currentRow = ref<Recordable>({});

  const authConfig = {
    issusd: `erp:${componentName}:issusd`,
    reject: `erp:${componentName}:reject`,
    inquiry: `erp:${componentName}:inquiry`,
  };

  const closeStatus = ref(['1']);
  const { createMessage } = useMessage();
  const { hasPermission } = usePermission();
  const { tableRef, gridOptions, registerModal, openModal, handleSuccess } = useListCrudHook({
    componentName,
    columns,
    searchFormSchema,
    pageApi: repairOutsourcePageApi,
    vxeGridOptions: {
      height: 450,
      rowConfig: {
        keyField: 'bizNo',
      },
      radioConfig: {
        trigger: 'row',
      },
      toolbarConfig: {
        buttons: [
          {
            content: '询价完成',
            buttonRender: {
              name: 'AButton',
              props: {
                type: 'primary',
                preIcon: 'mdi:account-check-outline',
              },
              attrs: {
                class: 'ml-2',
              },
              events: {
                click: handleInquiry,
              },
            },
            visible: hasPermission(authConfig.inquiry),
          },
          {
            buttonRender: {
              name: 'ACheckboxGroup',
              props: () => ({
                options: [
                  {
                    label: '是否过滤关闭',
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
      },
    },
    filters: () => {
      return {
        'orderStatus@IN': closeStatus.value.length > 0 ? '10,20,30,50' : undefined,
      };
    },
    module: 'erp',
  });

  async function handleIssusd(row) {
    try {
      await repairOutsourceIssuedApi(row);
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  const handleReject = async (row) => {
    try {
      await repairOutsourceRejectApi(row);
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  };

  async function handleInquiry() {
    try {
      if (!currentRow.value.bizNo) {
        return;
      }
      if (currentRow.value.orderStatus !== '20') {
        createMessage.error('只能操作询价中的订单');
        return;
      }
      await repairOutsourceInquiryEndApi(currentRow.value.bizNo);
      handleSuccess();
    } catch (error) {
      console.log(error);
    }
  }

  async function handleDetail(row) {
    try {
      const res = await getRepairByBizNoApi(row.originalNo);
      openModal(true, {
        record: res,
        isUpdate: true,
        disabled: true,
      });
    } catch (error) {
      console.log(error);
    }
  }

  function onCurrentChange({ row }) {
    currentRow.value = row || {};
  }

  function onDataChange({ visibleData }) {
    currentRow.value = visibleData[0] || {};
    tableRef.value?.setRadioRow(visibleData[0]);
    tableRef.value?.setCurrentRow(visibleData[0]);
  }
</script>

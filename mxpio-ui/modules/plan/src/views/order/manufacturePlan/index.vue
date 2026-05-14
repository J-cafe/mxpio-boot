<template>
  <div class="m-3">
    <VxeBasicTable ref="tableRef" v-bind="gridOptions">
      <template #action="{ row }">
        <TableAction
          :outside="true"
          :actions="[
            {
              label: '编辑',
              onClick: handleEdit.bind(null, row),
              auth: auth.edit,
            },
            {
              label: '拆分',
              onClick: handleSplit.bind(null, row),
              auth: authConfig.split,
            },
          ]"
        />
      </template>
    </VxeBasicTable>
    <ManufacturePlanModal @register="registerModal" @success="handleSuccess" />
    <ManufacturePlanSplitModal @register="registerSplitModal" @success="handleSuccess" />
    <FullcalendarModal
      ref="fullcalendarModalRef"
      @register="registerFullcalendarModal"
      @success="handleSuccess"
    />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { pmPageApi, pmConvertApi, pmEntrustApi } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './manufacturePlan.data';
  import ManufacturePlanModal from './ManufacturePlanModal.vue';
  import ManufacturePlanSplitModal from './ManufacturePlanSplitModal.vue';
  import FullcalendarModal from './FullcalendarModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'ManufacturePlanList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const [registerSplitModal, { openModal: openSplitModal }] = useModal();
  const [registerFullcalendarModal, { openModal: openFullcalendarModal }] = useModal();
  const authConfig = {
    split: `erp:${componentName}:split`,
  };
  const { auth, gridOptions, registerModal, handleEdit, handleSuccess, tableRef } = useListCrudHook(
    {
      componentName,
      columns,
      searchFormSchema,
      pageApi: pmPageApi,
      vxeGridOptions: {
        rowConfig: {
          keyField: 'planNo',
        },
        toolbarConfig: {
          buttons: [
            {
              content: '转生产订单',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:briefcase-swap-outline',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: () => {
                    handleToOrder();
                  },
                },
              },
            },
            {
              content: '转委外计划',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:briefcase-swap-outline',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: () => {
                    handleToOutsource();
                  },
                },
              },
            },
            {
              content: '日历',
              buttonRender: {
                name: 'AButton',
                props: {
                  type: 'primary',
                  preIcon: 'mdi:calendar-month',
                },
                attrs: {
                  class: 'ml-2',
                },
                events: {
                  click: () => {
                    openFullcalendarModal(true);
                  },
                },
              },
            },
          ],
          import: false,
        },
      },
      filters: {
        'outsourceFlag@EQ': '0',
      },
      module: 'erp',
    },
  );

  async function handleToOrder() {
    try {
      const select = tableRef.value?.getCheckboxRecords() || [];
      if (select.length > 0) {
        const planNos: string[] = [];
        select.forEach((item) => {
          if (!item.quantity || !item.deliverDate || !item.mainWorkshop || !item.workCenterCode) {
            planNos.push(item.planNo);
          }
        });
        if (planNos.length > 0) {
          createMessage.warning('计划单号：' + planNos.join() + '数据不完整');
          return;
        }
        await pmConvertApi(select);
        tableRef.value?.commitProxy('query');
      } else {
        createMessage.warning('请选择计划');
      }
    } catch (error) {
      console.log(error);
    }
  }

  async function handleToOutsource() {
    try {
      const select = tableRef.value?.getCheckboxRecords() || [];
      if (select.length > 0) {
        const outsourceAbles: string[] = [];
        select.forEach((item) => {
          item.outsourceAble !== '1' && outsourceAbles.push(item.itemCode);
        });
        if (outsourceAbles.length > 0) {
          createMessage.warning(`物料：${[...new Set(outsourceAbles)].join()}不能转委外`);
          return;
        }
        await pmEntrustApi(select);
        tableRef.value?.commitProxy('query');
      } else {
        createMessage.warning('请选择计划');
      }
    } catch (error) {
      console.log(error);
    }
  }

  async function handleSplit(row) {
    try {
      openSplitModal(true, {
        record: row,
        isUpdate: true,
      });
    } catch (error) {
      console.log(error);
    }
  }
</script>

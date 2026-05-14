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
    <OutsourcePlanModal @register="registerModal" @success="handleSuccess" />
    <OutsourcePlanSplitModal @register="registerSplitModal" @success="handleSuccess" />
    <FullcalendarModal @register="registerFullcalendarModal" @success="handleSuccess" />
    <ToOutsourceOrderModal @register="registerToOutsourceOrderModal" @success="handleSuccess" />
  </div>
</template>
<script lang="ts" setup>
  import { TableAction, VxeBasicTable, useModal } from '@mxpio/components';
  import { pmPageApi, supplyListByItem } from '@mxpio/bizcommon';
  import { useListCrudHook } from '@mxpio/common';
  import { columns, searchFormSchema } from './outsourcePlan.data';
  import OutsourcePlanModal from './OutsourcePlanModal.vue';
  import OutsourcePlanSplitModal from './OutsourcePlanSplitModal.vue';
  import FullcalendarModal from './FullcalendarModal.vue';
  import ToOutsourceOrderModal from './ToOutsourceOrderModal.vue';
  import { useMessage } from '@mxpio/hooks';

  const componentName = 'OutsourcePlanList';
  defineOptions({ name: componentName });
  const { createMessage } = useMessage();
  const [registerSplitModal, { openModal: openSplitModal }] = useModal();
  const [registerFullcalendarModal, { openModal: openFullcalendarModal }] = useModal();
  const [registerToOutsourceOrderModal, { openModal: openToOutsourceOrderModal }] = useModal();

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
              content: '转委外订单',
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
        'outsourceFlag@EQ': '1',
      },
      module: 'erp',
    },
  );

  async function handleToOrder() {
    try {
      const select = tableRef.value?.getCheckboxRecords() || [];
      if (select.length > 0) {
        const planNos: string[] = [];
        const itemCodes: string[] = [];
        select.forEach((item) => {
          if (!item.quantity || !item.deliverDate) {
            planNos.push(item.planNo);
          }
          !itemCodes.includes(item.itemCode) && itemCodes.push(item.itemCode);
        });
        if (planNos.length > 0) {
          createMessage.warning('计划单号：' + planNos.join() + '数据不完整');
          return;
        }
        const res = await supplyListByItem(itemCodes.join());
        if (res.length > 0) {
          openToOutsourceOrderModal(true, {
            record: select,
            supplyList: res,
          });
        } else {
          createMessage.warning('所选物料无相同加工商，请重新选择');
        }
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

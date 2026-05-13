<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="订单BOM" forceRender>
        <BomDetailTable
          v-if="formDataRef.orderType !== '03'"
          ref="tableRef"
          :formDataRef="formDataRef" />
        <ReworkBomDetailTable v-else ref="reworktableRef" :formDataRef="formDataRef"
      /></a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { saveManufactureOrder } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './manufactureOrder.data';
  import { dateUtil } from '@mxpio/utils';
  import BomDetailTable from './BomDetailTable.vue';
  import ReworkBomDetailTable from './ReworkBomDetailTable.vue';
  import { ref } from 'vue';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'ManufactureOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof BomDetailTable>>();
  const reworktableRef = ref<InstanceType<typeof ReworkBomDetailTable>>();
  const userStore = useUserStore();
  const { username } = userStore.getUserInfo || {};
  const title = ref('生产订单');

  const {
    registerForm,
    registerModal,
    setFieldsValue,
    getTitle,
    isDisabled,
    handleSubmit,
    formDataRef,
  } = useModalFormCrud({
    title: title,
    formSchema,
    saveApi: saveManufactureOrder,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      referenceDate: dateUtil().add(1, 'day'),
      orderDate: dateUtil().format('YYYY-MM-DD'),
      planner: username,
      orderType: '01',
      // bpmnStatus: '01',
      orderStatus: '10',
      closeStatus: 'open',
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          const { record } = data;
          if (record.orderType === '03') {
            reworktableRef.value?.setData(data);
          } else {
            tableRef.value?.setData(data);
          }
        },
        validate: () => {
          if (formDataRef.value.orderType === '03') {
            return reworktableRef.value?.validate() || Promise.resolve();
          } else {
            return Promise.resolve();
          }
        },
        getSubData: () => {
          if (formDataRef.value.orderType === '03') {
            return reworktableRef.value?.getData() || {};
          } else {
            return {};
          }
        },
      },
    ],
    openAfter: (data) => {
      const { type, record } = data;
      if (type === 'rework') {
        setFieldsValue({
          orderType: '03',
        });
      }
      if (record.orderType === '03' || type === 'rework') {
        title.value = '返工订单';
      }
    },
  });
</script>

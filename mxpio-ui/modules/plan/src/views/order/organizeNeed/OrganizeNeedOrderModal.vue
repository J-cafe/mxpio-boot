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
      <a-tab-pane key="detailTable" tab="需求明细" forceRender>
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { odSaveApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './organizeNeed.data';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'OrganizeNeedOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '组织需求',
    formSchema,
    saveApi: odSaveApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      demandType: '01',
      bpmnStatus: '01',
      orderStatus: '10',
      closeStatus: 'open',
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          tableRef.value?.setData(data);
        },
        validate: () => {
          return tableRef.value?.validate() || Promise.resolve();
        },
        getSubData: () => {
          return tableRef.value?.getData() || {};
        },
      },
    ],
  });
</script>

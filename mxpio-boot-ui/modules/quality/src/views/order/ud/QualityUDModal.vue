<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="isJudge ? '不良品处理单审核' : getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled || !!isJudge"
    :okText="isJudge ? '审核' : '确定'"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="不合格品明细" forceRender>
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { udSaveApi, udSaveAndPassApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './ud.data';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'QualityUDModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const isJudge = ref(false);

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '不良品处理单',
    formSchema,
    saveApi: (data) => {
      return isJudge.value ? udSaveAndPassApi(data) : udSaveApi(data);
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      bpmnStatus: '01',
    },
    openAfter: (data) => {
      const { judge } = data;
      isJudge.value = judge;
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

  // function handleSubmitUD() {
  //   if (isJudge.value) {
  //     if (!formDataRef.value.testResult) {
  //       return createMessage.warning('请先维护检验结论');
  //     }
  //     if (
  //       formDataRef.value.unqualifiedQuantity === '' ||
  //       formDataRef.value.unqualifiedQuantity === null ||
  //       formDataRef.value.unqualifiedQuantity === undefined
  //     ) {
  //       return createMessage.warning('请先维护不合格数量');
  //     }
  //     await qualityJudgeApi(formDataRef.value.bizNo);
  //     emit('success');
  //     closeModal();
  //     return;
  //   }
  //   return handleSubmit();
  // }
</script>

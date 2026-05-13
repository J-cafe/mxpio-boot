<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmitQuality"
    :showOkBtn="!isDisabled || !!isJudge"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm">
      <template #receive="{ model, disabled }">
        <a-input :value="getReceive(model)" :disabled="disabled" />
      </template>
    </BasicForm>
    <a-tabs>
      <a-tab-pane key="bydetect" tab="按样本" forceRender>
        <SimpleDetailTable ref="tableSimpleRef" />
      </a-tab-pane>
      <a-tab-pane key="bysimple" tab="按检测项" forceRender>
        <DetectDetailTable ref="tableDetectRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { qualitySaveApi, qualityJudgeApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './qualityOrder.data';
  import DetectDetailTable from './DetectDetailTable.vue';
  import SimpleDetailTable from './SimpleDetailTable.vue';
  import { ref } from 'vue';
  import { dateUtil } from '@mxpio/utils';
  import { useUserStore } from '@mxpio/stores';
  import { useMessage } from '@mxpio/hooks';
  import Big from 'big.js';

  defineOptions({ name: 'QualityOrderModal' });

  const emit = defineEmits(['success', 'register']);
  const tableDetectRef = ref<InstanceType<typeof DetectDetailTable>>();
  const tableSimpleRef = ref<InstanceType<typeof SimpleDetailTable>>();
  const userStore = useUserStore();
  const { username } = userStore.getUserInfo || {};
  const isJudge = ref(false);
  const { createMessage } = useMessage();

  const {
    registerForm,
    registerModal,
    getTitle,
    isDisabled,
    handleSubmit,
    setFieldsValue,
    formDataRef,
    closeModal,
  } = useModalFormCrud({
    title: '质检单',
    formSchema,
    saveApi: qualitySaveApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      const { isUpdate, disabled, record, judge } = data;
      isJudge.value = judge;
      if (isUpdate && !disabled && !record?.inspectionStartTime) {
        setFieldsValue({
          inspectionStartTime: dateUtil().format('YYYY-MM-DD HH:mm:ss'),
          inspector: username,
        });
      }
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          tableSimpleRef.value?.setData(data);
        },
        validate: () => {
          return tableSimpleRef.value?.validate() || Promise.resolve();
        },
        getSubData: () => {
          return tableSimpleRef.value?.getData() || {};
        },
      },
      {
        initSubData: (data: Recordable) => {
          tableDetectRef.value?.setData(data);
        },
        getSubData: () => {
          return {};
        },
      },
    ],
  });

  async function handleSubmitQuality() {
    if (isJudge.value) {
      if (!formDataRef.value.testResult) {
        return createMessage.warning('请先维护检验结论');
      }
      if (
        formDataRef.value.unqualifiedQuantity === '' ||
        formDataRef.value.unqualifiedQuantity === null ||
        formDataRef.value.unqualifiedQuantity === undefined
      ) {
        return createMessage.warning('请先维护不合格数量');
      }
      await qualityJudgeApi(formDataRef.value.bizNo);
      emit('success');
      closeModal();
      return;
    }
    return handleSubmit();
  }

  function getReceive(model) {
    return Big(model.receiveQuantity || 0)
      .plus(model.concessionQuantity || 0)
      .toNumber();
  }
</script>

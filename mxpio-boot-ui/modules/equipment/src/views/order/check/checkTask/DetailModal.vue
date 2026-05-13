<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { addEqpCheckTaskDetailApi, editEqpCheckTaskDetailApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { detailFormSchema } from './eqpCheckTask.data';
  import { ref } from 'vue';

  defineOptions({ name: 'DetailModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, setFieldsValue } =
    useModalFormCrud({
      title: '点检任务明细',
      formSchema: detailFormSchema,
      addApi: addEqpCheckTaskDetailApi,
      editApi: editEqpCheckTaskDetailApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        isRecord: '1',
      },
      openAfter: (data: Recordable) => {
        const { isUpdate, record } = data;
        formDataAll.value = data.record || {};
        if (isUpdate) {
          setFieldsValue({
            chooseEnable: record.chooseEnable.toString(),
            isRecord: record.isRecord.toString(),
          });
        }
      },
      classifyIntoFormData: (formData) => {
        formData.bizNo = props.bizNo;
        return {
          ...formDataAll.value,
          ...formData,
        };
      },
    });
</script>

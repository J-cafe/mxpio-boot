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
  import { addEqpBomApi, editEqpBomApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { bomFormSchema } from './eqpInfo.data';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpInfoBomModal' });

  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    eqpCode: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '设备备件',
    formSchema: bomFormSchema,
    addApi: addEqpBomApi,
    editApi: editEqpBomApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      formDataAll.value = data.record || {};
    },
    classifyIntoFormData: (formData) => {
      formData.eqpCode = props.eqpCode;
      return {
        ...formDataAll.value,
        ...formData,
      };
    },
  });
</script>

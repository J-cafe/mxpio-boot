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
  import { addEqpSopApi, editEqpSopApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { sopFormSchema } from './eqpBasics.data';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpBasicsSopModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    basicsCode: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '设备SOP',
    formSchema: sopFormSchema,
    addApi: addEqpSopApi,
    editApi: editEqpSopApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      formDataAll.value = data.record || {};
    },
    classifyIntoFormData: (formData) => {
      formData.basicsCode = props.basicsCode;
      return {
        ...formDataAll.value,
        ...formData,
      };
    },
  });
</script>

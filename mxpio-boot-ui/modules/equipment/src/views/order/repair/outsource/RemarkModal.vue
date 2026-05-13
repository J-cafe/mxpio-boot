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
  import { addRepairOutsourceRemarkApi, editRepairOutsourceRemarkApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { remarkFormSchema } from './outsource.data';
  import { ref } from 'vue';

  defineOptions({ name: 'OtherRepairRemarkModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '备注',
    formSchema: remarkFormSchema,
    addApi: addRepairOutsourceRemarkApi,
    editApi: editRepairOutsourceRemarkApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      formDataAll.value = data.record || {};
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

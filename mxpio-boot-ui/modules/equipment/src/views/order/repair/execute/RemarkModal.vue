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
  import { addEqpRepairRemarkApi, editEqpRepairRemarkApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { remarkFormSchema } from './execute.data';
  import { ref } from 'vue';

  defineOptions({ name: 'EqpRepairRemarkModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '重要备注',
    formSchema: remarkFormSchema,
    addApi: addEqpRepairRemarkApi,
    editApi: editEqpRepairRemarkApi,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      formDataAll.value = data.record || {};
    },
    classifyIntoFormData: (formData) => {
      formData.bizNo = props.bizNo;
      return {
        ...formDataAll.value, //避免获取的表单数据缺少原始数据字段，例如ID
        ...formData,
      };
    },
  });
</script>

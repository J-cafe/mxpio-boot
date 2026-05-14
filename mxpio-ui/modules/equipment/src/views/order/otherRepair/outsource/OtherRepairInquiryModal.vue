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
  import { editOtherOutsourceInquiryApi, addOtherOutsourceInquiryApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { InquiryFormSchema } from './outsource.data';
  import { ref } from 'vue';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'OtherRepairInquiryModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    bizNo: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, isUpdate } =
    useModalFormCrud({
      title: '询价',
      formSchema: InquiryFormSchema,
      addApi: addOtherOutsourceInquiryApi,
      editApi: editOtherOutsourceInquiryApi,
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
          crudType: isUpdate.value ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
        };
      },
    });
</script>

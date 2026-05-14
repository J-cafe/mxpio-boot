<template>
  <BasicModal
    width="800px"
    title="请购单审核"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { auditBuyRequest } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { checkFormSchema } from './buyRequest.data';

  defineOptions({ name: 'BuyRequestAuditModal' });
  const props = defineProps({
    bizNo: {
      type: String,
      default: '',
    },
  });

  const emit = defineEmits(['success', 'register']);
  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    formSchema: checkFormSchema,
    saveApi: (data) => {
      return auditBuyRequest(props.bizNo, {
        bizNo: props.bizNo,
        ...data,
      });
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
  });
</script>

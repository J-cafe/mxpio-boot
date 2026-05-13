<template>
  <BasicModal
    width="800px"
    title="驳回申请"
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
  import { auditBuyReject } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { rejectFormSchema } from './buyRequestDetail.data';

  defineOptions({ name: 'BuyRequestRejectModal' });
  const props = defineProps({
    rows: {
      type: Array as PropType<Recordable[]>,
      default: () => [],
    },
  });

  const emit = defineEmits(['success', 'register']);
  const { registerForm, registerModal, isDisabled, handleSubmit } = useModalFormCrud({
    formSchema: rejectFormSchema,
    saveApi: (data) => {
      const ids: string[] = [];
      props.rows.forEach((item) => {
        ids.push(`${item.bizNo}:${item.lineNo}`);
      });
      return auditBuyReject({
        ids: ids.join(),
        ...data,
      });
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    defaultValues: {
      auditOpinion: '03',
    },
  });
</script>

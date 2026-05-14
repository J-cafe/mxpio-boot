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
  import { addEqpCheckCategoryDetailApi, editEqpCheckCategoryDetailApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { detailFormSchema } from './eqpCheckCategory.data';
  import { ref } from 'vue';

  defineOptions({ name: 'DetailModal' });
  const formDataAll = ref({});
  const emit = defineEmits(['success', 'register']);
  const props = defineProps({
    categoryId: {
      type: String,
      default: () => '',
    },
  });

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, setFieldsValue } =
    useModalFormCrud({
      title: '点检方案明细',
      formSchema: detailFormSchema,
      addApi: addEqpCheckCategoryDetailApi,
      editApi: editEqpCheckCategoryDetailApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
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
        formData.categoryId = props.categoryId;
        return {
          ...formDataAll.value,
          ...formData,
        };
      },
    });
</script>

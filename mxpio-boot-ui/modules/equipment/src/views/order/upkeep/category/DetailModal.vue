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
  import { addEqpUpkeepCategoryDetailApi, editEqpUpkeepCategoryDetailApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { detailFormSchema } from './upkeepCategory.data';
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
      title: '保养方案明细',
      formSchema: detailFormSchema,
      addApi: addEqpUpkeepCategoryDetailApi,
      editApi: editEqpUpkeepCategoryDetailApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data: Recordable) => {
        const { isUpdate, record } = data;
        formDataAll.value = data.record || {};
        if (isUpdate) {
          setFieldsValue({
            chooseEnable: record.chooseEnable?.toString(),
            isRecord: record.isRecord?.toString(),
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

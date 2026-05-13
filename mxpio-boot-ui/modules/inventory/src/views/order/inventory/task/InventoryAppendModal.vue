<template>
  <BasicModal
    width="600px"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="追加盘点明细"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm">
      <template #lotNo="{ model, field, disabled }">
        <a-input-search
          v-model:value="model[field]"
          :disabled="disabled"
          placeholder="请选择批次"
          @search="handleLot"
        />
      </template>
    </BasicForm>
    <LotSelectModal @register="registerLotModal" @select="selectLot" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, useModal } from '@mxpio/components';
  import { addInvLineApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { appendFormSchema } from './inventoryTask.data';
  import { InputSearch as AInputSearch } from 'ant-design-vue';
  import LotSelectModal from './LotSelectModal.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'InventoryAppendModal' });

  const invData = ref<Recordable>({});
  const lotData = ref<Recordable>({});
  const emit = defineEmits(['success', 'register']);
  const [registerLotModal, { openModal: openLotModal }] = useModal();
  const { registerForm, registerModal, isDisabled, handleSubmit, getFieldsValue, setFieldsValue } =
    useModalFormCrud({
      title: '',
      formSchema: appendFormSchema,
      saveApi: addInvLineApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        invData.value = data.invData;
      },
      classifyIntoFormData: (data) => {
        return {
          inventoryCode: invData.value.inventoryCode,
          whCode: invData.value.whCode,
          subLotIndex: lotData.value.subLotIndex,
          quantity: lotData.value.quantity,
          ...data,
        };
      },
    });

  function handleLot() {
    const { itemCode } = getFieldsValue();
    openLotModal(true, {
      record: {
        itemCode: itemCode,
        whCode: invData.value.whCode,
      },
    });
  }

  function selectLot(data) {
    setFieldsValue({
      lotNo: data.lotNo,
    });
    lotData.value = data;
  }
</script>

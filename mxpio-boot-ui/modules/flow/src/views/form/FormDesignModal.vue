<template>
  <BasicModal
    title="设计表单"
    wrapClassName="containers"
    :defaultFullscreen="true"
    v-bind="$attrs"
    destroyOnClose
    :footer="null"
    @register="registerModal"
    @cancel="handleCancel"
  >
    <VFormDesign v-if="showBpmn" @save="handleSubmit" :formConfigDef="formConfigDef" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VFormDesign } from '@mxpio/components';
  import { editForm } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  // import { message } from 'ant-design-vue';
  // import VFormDesign from '../../components/form-design/components/VFormDesign/index.vue';

  defineOptions({ name: 'FormDesignModal' });

  const showBpmn = ref(false);
  const emit = defineEmits(['success', 'register']);

  const form = ref({
    model: '',
  });

  const formConfigDef = ref();

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    const { record } = data;
    form.value = record;
    formConfigDef.value = record.model && JSON.parse(record.model);
    showBpmn.value = true;
  });

  async function handleSubmit(data) {
    try {
      setModalProps({ confirmLoading: true });
      editForm({
        ...form.value,
        model: JSON.stringify(data),
        crudType: CuryTypeEnum.UPDATE,
      });
      closeModal();
      emit('success');
    } finally {
      showBpmn.value = false;
      setModalProps({ confirmLoading: false });
    }
  }

  async function handleCancel() {
    showBpmn.value = false;
    closeModal();
  }
</script>
<style lang="less">
  .containers {
    .ant-modal .ant-modal-body > .scrollbar {
      padding: 0;
    }

    .ant-modal .ant-modal-header {
      margin-bottom: 0;
    }
  }
</style>

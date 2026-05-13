<template>
  <BasicModal
    title="预览表单"
    wrapClassName="containers"
    v-bind="$attrs"
    destroyOnClose
    :footer="null"
    @register="registerModal"
    @cancel="handleCancel"
  >
    <VFormCreate v-if="showBpmn" :formConfig="formConfigDef" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VFormCreate } from '@mxpio/components';
  // import VFormCreate from '../../components/form-design/components/VFormCreate/index.vue';

  defineOptions({ name: 'FormViewModal' });

  const showBpmn = ref(false);

  const form = ref({
    model: '',
  });

  const formConfigDef = ref();

  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    const { record } = data;
    form.value = record;
    formConfigDef.value = record.model ? JSON.parse(record.model) : {};
    showBpmn.value = true;
  });

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

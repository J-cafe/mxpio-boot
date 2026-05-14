<template>
  <BasicModal
    title="设计工作流"
    wrapClassName="containers"
    :defaultFullscreen="true"
    v-bind="$attrs"
    destroyOnClose
    :footer="null"
    @register="registerModal"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <bpmn v-if="showBpmn" :flow="flow" :xml="flow.xml" @save="handleSubmit" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '@mxpio/components';
  import { editBpmn } from '@mxpio/bizcommon';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import bpmn from '../../components/bpmn/bpmn.vue';

  defineOptions({ name: 'BpmnDesignModal' });

  const showBpmn = ref(false);
  const emit = defineEmits(['success', 'register']);

  const flow = ref({
    xml: '',
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async (data) => {
    const { record } = data;
    flow.value = record;
    showBpmn.value = true;
  });

  async function handleSubmit(data) {
    try {
      setModalProps({ confirmLoading: true });
      editBpmn({
        ...flow.value,
        xml: data.xml,
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

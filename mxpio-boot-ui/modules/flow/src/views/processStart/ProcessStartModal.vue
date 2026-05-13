<template>
  <BasicModal
    :title="title"
    wrapClassName="containers"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @cancel="handleCancel"
    @ok="handleSubmit"
  >
    <a-tabs tabPosition="left">
      <a-tab-pane key="1" tab="表单信息">
        <VFormCreate
          ref="vFormCreate"
          v-if="showBpmn"
          :formConfig="formConfigDef"
          v-model:formModel="formData"
        />
      </a-tab-pane>
      <a-tab-pane key="2" tab="流程图">
        <bpmnViewer :move="true" :xml="bpmnXmlStr" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VFormCreate } from '@mxpio/components';
  // import VFormCreate from '../../components/form-design/components/VFormCreate/index.vue';
  import bpmnViewer from '../../components/bpmn/bpmnViewer.vue';
  import { defBpmn, startBpmn } from '@mxpio/bizcommon';

  defineOptions({ name: 'ProcessStartModal' });

  const emit = defineEmits(['success', 'register']);
  const showBpmn = ref(false);
  const vFormCreate = ref();
  const title = ref('');
  const form = ref({
    model: '',
    code: '',
  });

  const formData = ref({
    // _icon_picker_2: '',
    // _input_count_down_1: '',
  });

  const formConfigDef = ref(); //表单配置信息
  const bpmnXmlStr = ref(); //流程图xml
  const [registerModal, { closeModal }] = useModalInner(async (data) => {
    const { record } = data;
    form.value = record;
    const res = await defBpmn(record.code);
    formConfigDef.value = res.startFormModelDef?.model
      ? JSON.parse(res.startFormModelDef.model)
      : {};
    bpmnXmlStr.value = res.source;
    showBpmn.value = true;
    title.value = `发起流程：${record.name}`;
    // formConfigDef.value = record.model ? JSON.parse(record.model) : {};
  });

  async function handleCancel() {
    showBpmn.value = false;
    closeModal();
  }

  const handleSubmit = async () => {
    try {
      const _data = await vFormCreate?.value.validate?.();
      await start(_data);
      closeModal();
      emit('success');
    } catch (error) {
      console.log('error', error);
    }
  };

  function start(_data: Recordable) {
    return startBpmn(form.value.code, _data);
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

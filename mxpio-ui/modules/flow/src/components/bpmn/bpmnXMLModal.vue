<template>
  <BasicModal
    title="预览XML"
    wrapClassName="containers"
    width="900px"
    v-bind="$attrs"
    destroyOnClose
    :footer="null"
    @register="registerModal"
  >
    <CodeEditor v-model:value="bpmnStr" :mode="modeValue" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, CodeEditor, MODE } from '@mxpio/components';

  defineOptions({ name: 'BpmnXMLModal' });

  const modeValue = ref<MODE>(MODE.XML);
  let bpmnStr = ref();

  const [registerModal] = useModalInner(async (data) => {
    bpmnStr.value = data.bpmnStr;
    console.log(bpmnStr.value);
  });
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

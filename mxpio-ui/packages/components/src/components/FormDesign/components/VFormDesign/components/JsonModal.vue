<!--
 * @Description: 渲染JSON数据
-->
<template>
  <Modal
    title="JSON数据"
    :footer="null"
    :open="state.visible"
    @cancel="handleCancel"
    :destroyOnClose="true"
    wrapClassName="v-code-modal"
    style="top: 20px"
    width="850px"
  >
    <PreviewCode :editorJson="editorJson" />
  </Modal>
</template>
<script lang="ts" setup>
  import { computed, reactive, toRefs, defineExpose, defineEmits } from 'vue';
  import PreviewCode from './PreviewCode.vue';
  import { IFormConfig } from '../../../typings/v-form-component';
  import { formatRules, removeAttrs } from '../../../utils';
  import { Modal } from 'ant-design-vue';

  const state = reactive<{
    visible: boolean;
    jsonData: IFormConfig;
  }>({
    visible: false, // 控制json数据弹框显示
    jsonData: {} as IFormConfig, // json数据
  });

  const emit = defineEmits(['cancel']);

  /**
   * 显示Json数据弹框
   * @param jsonData
   */
  const showModal = (jsonData: IFormConfig) => {
    formatRules(jsonData.schemas);
    state.jsonData = jsonData as any;
    state.visible = true;
  };

  // 计算json数据
  const editorJson = computed(() => {
    // @ts-ignore
    return JSON.stringify(removeAttrs(state.jsonData), null, '\t');
  });

  // 关闭弹框
  const handleCancel = () => {
    state.visible = false;
    emit('cancel');
  };
  defineExpose({
    ...toRefs(state),
    editorJson,
    handleCancel,
    showModal,
  });

  // export default defineComponent({
  //   name: 'JsonModal',
  //   components: {
  //     PreviewCode,
  //     Modal,
  //   },
  //   emits: ['cancel'],
  //   setup(_props, { emit }) {

  //     return { ...toRefs(state), editorJson, handleCancel, showModal };
  //   },
  // });
</script>

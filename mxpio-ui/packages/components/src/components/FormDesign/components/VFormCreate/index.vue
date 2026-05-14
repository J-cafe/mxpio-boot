<!--
 * @Description: 表单渲染器，根据json生成表单
-->
<template>
  <div>
    <DesignForm
      v-if="formConfig.formType === 'design'"
      ref="designForm"
      :fApi="fApi"
      :formConfig="formConfig"
      :formModel="formModel"
      :bpmnTask="bpmnTask"
    />
    <CustomForm
      v-else
      ref="customForm"
      :formConfig="formConfig"
      :formModel="formModel"
      :bpmnTask="bpmnTask"
    />
  </div>
</template>
<script lang="ts" setup>
  import { PropType, ref } from 'vue';
  import { IFormConfig } from '../../typings/v-form-component';
  import CustomForm from './components/CustomForm.vue';
  import DesignForm from './components/DesignForm.vue';

  const props = defineProps({
    fApi: {
      type: Object,
    },
    formModel: {
      type: Object,
      default: () => ({}),
    },
    formConfig: {
      type: Object as PropType<IFormConfig>,
      required: true,
    },
    bpmnTask: {
      //流程节点数据
      type: Object,
      default: () => ({}),
    },
  });

  const designForm = ref<InstanceType<typeof DesignForm> | null>(null);
  const customForm = ref<InstanceType<typeof CustomForm> | null>(null);
  const validate = async function () {
    if (props.formConfig.formType === 'design') {
      const data = await designForm.value?.validate();
      return data;
    } else {
      const data = await customForm.value?.validate();
      return data;
    }
  };

  const bpmnSubmitAfter = async function (data) {
    if (props.formConfig.formType === 'custom') {
      return customForm.value?.bpmnSubmitAfter(data);
    }
  };

  defineExpose({
    validate,
    bpmnSubmitAfter,
  });
</script>

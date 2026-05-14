<template>
  <div>
    <component ref="customForm" :is="componentItem?.component" />
    <!-- :formModel="formModel"
      :formConfig="formConfig" -->
  </div>
</template>
<script lang="ts" setup>
  import { ref, computed, watch, nextTick } from 'vue';
  import { customFormsMap } from '../../../core/formItemConfig';
  import { IFormConfig } from '../../../typings/v-form-component';

  const props = defineProps({
    formModel: {
      type: Object,
      default: () => ({}),
    },
    formConfig: {
      type: Object as PropType<IFormConfig>,
      required: true,
    },
  });

  // 监听formModel变化，更新自定义表单数据
  watch(
    () => props.formModel,
    (newValue) => {
      nextTick(() => {
        if (customForm.value) {
          customForm.value.setFormData({
            disabled: props.formConfig.disabled,
            isUpdate: true,
            isBpmn: true,
            record: newValue,
          });
        }
      });
    },
    {
      deep: true,
      immediate: true,
    },
  );

  const customForm = ref();
  const componentItem = computed(() => customFormsMap.get(props.formConfig.formPath as string));

  // 暴露自定义表单校验方法
  const validate = async function () {
    return customForm.value.validate();
  };

  // 暴露自定义表单bpmnSubmitAfter方法，更新自定义表单数据
  const bpmnSubmitAfter = async function (data) {
    return customForm.value?.bpmnSubmitAfter?.(data);
  };

  defineExpose({
    validate,
    bpmnSubmitAfter,
  });
</script>

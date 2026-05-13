<template>
  <PageWrapper title="上传组件示例">
    <Alert message="基础示例" />
    <BasicUpload
      :maxSize="20"
      :maxNumber="10"
      v-model:value="fileList"
      @change="handleChange"
      :api="uploadApi"
      class="my-5"
      :accept="['image/*']"
      :disabled="true"
    />

    <Alert message="嵌入表单,加入表单校验" />

    <BasicForm @register="register" class="my-5" />
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { BasicUpload, BasicForm, FormSchema, useForm, PageWrapper } from '@mxpio/components';
  import { useMessage } from '@mxpio/hooks/src/web/useMessage';
  import { Alert } from 'ant-design-vue';
  import { uploadApi } from '@mxpio/api';
  import { ref } from 'vue';

  const schemas: FormSchema[] = [
    {
      field: 'field1',
      component: 'Upload',
      label: '字段1',
      colProps: {
        span: 8,
      },
      rules: [{ required: true, message: '请选择上传文件' }],
      componentProps: {
        api: uploadApi,
      },
    },
  ];
  const { createMessage } = useMessage();
  const [register] = useForm({
    labelWidth: 120,
    schemas,
    actionColOptions: {
      span: 16,
    },
  });
  const fileList = ref<string[]>([
    '81a4cfbf-70ba-44b6-abc8-1a51078e87c4',
    '2c99562a-5c86-4cab-9531-22df02bff3a0',
  ]);
  function handleChange(list: string[]) {
    createMessage.info(`已上传文件${JSON.stringify(list)}`);
  }
</script>

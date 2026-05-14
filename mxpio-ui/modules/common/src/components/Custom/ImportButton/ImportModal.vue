<template>
  <BasicModal
    title="导入"
    okText="导入"
    width="400px"
    destroyOnClose
    v-bind="$attrs"
    @register="registerModal"
    @ok="handleSubmit"
  >
    <BasicForm @register="registerForm" class="import-warp">
      <template #upload="{ model, field }">
        <a-upload
          v-model:file-list="model[field]"
          name="file"
          :accept="accept"
          :maxCount="1"
          :before-upload="beforeUpload"
        >
          <a-button>
            <upload-outlined />
            {{ $t('component.upload.choose') }}
          </a-button>
        </a-upload>
      </template>
    </BasicForm>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { Upload as AUpload } from 'ant-design-vue';
  import { UploadOutlined } from '@ant-design/icons-vue';
  import { BasicModal, useModalInner, BasicForm, useForm, FormSchema } from '@mxpio/components';
  import { CuryTypeEnum } from '@mxpio/enums/src/curyEnum';
  import { $t } from '@mxpio/locales';

  defineOptions({ name: 'ImportModal' });

  const formSchema: FormSchema[] = [
    {
      field: 'file',
      label: '文件名',
      slot: 'upload',
      required: true,
    },
    {
      field: 'curd',
      label: '参数设置',
      component: 'RadioGroup',
      componentProps: {
        options: [
          {
            label: '新增',
            value: CuryTypeEnum.SAVE,
          },
          {
            label: '覆盖',
            value: CuryTypeEnum.UPDATE,
          },
        ],
      },
      required: true,
    },
  ];

  const emit = defineEmits(['success', 'register']);
  const accept = '.xls,.xlsx';

  const [registerForm, { resetFields, setFieldsValue, validate }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formSchema,
    showActionButtonGroup: false,
  });

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    resetFields();
    setFieldsValue({
      curd: CuryTypeEnum.SAVE,
    });
    setModalProps({ confirmLoading: false });
  });

  async function handleSubmit() {
    try {
      let values = await validate();
      emit('success', {
        file: values.file[0]?.originFileObj,
        curd: values.curd,
      });
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  function beforeUpload() {
    // 阻止上传
    return false;
  }
</script>
<style lang="less">
  .import-warp {
    .ant-form-show-help {
      display: none;
    }
  }
</style>

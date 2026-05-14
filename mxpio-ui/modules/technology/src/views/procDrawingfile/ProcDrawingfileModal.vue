<template>
  <BasicModal
    width="800px"
    :title="getTitle"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormSchema } from '@mxpio/components';
  import { addDrawingfile, signInDrawingfile } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { ref, unref, computed } from 'vue';
  import { CuryTypeEnum } from '@mxpio/enums';

  defineOptions({ name: 'ProcDrawingfileModal' });

  const props = defineProps({
    prodrout: {
      // 工艺路线工序
      type: Object,
      default: () => ({}),
    },
    rout: {
      // 工艺路线主单
      type: Object,
      default: () => ({}),
    },
  });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'file',
      label: '上传图纸',
      component: 'Upload',
      required: true,
      componentProps: ({ formActionType }) => {
        return {
          onChange: (ids: string, rows: Recordable) => {
            console.log(ids, rows);
            const { setFieldsValue } = formActionType;
            setFieldsValue({
              fileName: rows[0]?.name || '',
              length: rows[0]?.size || '',
              fileNo: ids || '',
            });
          },
        };
      },
      colProps: {
        span: 24,
      },
    },
    {
      field: 'fileName',
      label: '图纸名称',
      component: 'Input',
      colProps: {
        span: 24,
      },
      ifShow: false,
    },
    {
      field: 'length',
      label: '文件大小',
      component: 'Input',
      colProps: {
        span: 24,
      },
      ifShow: false,
    },
    {
      field: 'fileNo',
      label: '图纸编号',
      component: 'Input',
      colProps: {
        span: 24,
      },
      ifShow: false,
    },
    {
      field: 'remarks',
      label: '描述',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      ifShow: () => {
        return !isUpdate.value;
      },
    },
    {
      field: 'signInDesc',
      label: '签入说明',
      component: 'InputTextArea',
      colProps: {
        span: 24,
      },
      ifShow: () => {
        return isUpdate.value;
      },
    },
  ];
  const formData = ref<Recordable>({});
  const { registerForm, registerModal, isDisabled, handleSubmit, isUpdate } = useModalFormCrud({
    title: '上传图纸',
    formSchema,
    addApi: addDrawingfile,
    editApi: signInDrawingfile,
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    openAfter: (data) => {
      const { record } = data;
      formData.value = record || {};
    },
    classifyIntoFormData: (values) => {
      return {
        family: formData.value.family,
        ...values,
        drawingFileId: formData.value.id,
        routProcId: props.prodrout.id,
        itemCode: props.rout.itemCode,
        crudType: unref(isUpdate) ? CuryTypeEnum.UPDATE : CuryTypeEnum.SAVE,
        bpmnStatus: unref(isUpdate) ? '01' : null,
        version: props.rout.version,
      };
    },
  });

  const getTitle = computed(() => {
    return isUpdate.value ? '签入图纸' : '上传图纸';
  });
</script>

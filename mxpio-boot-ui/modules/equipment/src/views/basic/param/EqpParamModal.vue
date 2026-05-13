<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs v-if="formDataRef?.chooseEnable === '1' || formDataRef?.chooseEnable === 1">
      <a-tab-pane key="1" tab="公共参数选项">
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { addEqpParam } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import DetailTable from './DetailTable.vue';

  defineOptions({ name: 'EqpParamModal' });
  const emit = defineEmits(['success', 'register']);

  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const formSchema: FormSchema[] = [
    {
      field: 'paramName',
      label: '参数名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'paramType',
      label: '参数类型',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_COMM_TYPE',
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'unit',
      label: '单位',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_COMM_UNIT',
        };
      },
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.paramType === '1';
      },
    },
    {
      field: 'max',
      label: '最大值',
      component: 'InputNumber',
      required: true,
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.paramType === '1';
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (value && formDataRef.value?.min && value <= formDataRef.value?.min) {
              return Promise.reject('最大值不能小于最小值');
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'min',
      label: '最小值',
      component: 'InputNumber',
      required: true,
      colProps: {
        span: 8,
      },
      ifShow: (formData) => {
        return formData.values?.paramType === '1';
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            if (value && formDataRef.value?.max && value >= formDataRef.value?.max) {
              return Promise.reject('最小值不能大于最大值');
            }
            return Promise.resolve();
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'defaultValue',
      label: '缺省值',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'chooseEnable',
      label: '存在选项',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_COMMON_YESNO',
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'method',
      label: '方法',
      component: 'Input',
      colProps: {
        span: 8,
      },
    },
    {
      field: 'orderType',
      label: '参数分类',
      component: 'DictSelect',
      required: true,
      componentProps: () => {
        return {
          dictCode: 'ERP_EQUIPMENT_COMM_ORDER_TYPE',
        };
      },
      colProps: {
        span: 8,
      },
    },
    {
      field: 'remarks',
      label: '备注',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
    },
  ];
  const {
    registerForm,
    registerModal,
    setFieldsValue,
    getTitle,
    isDisabled,
    handleSubmit,
    formDataRef,
  } = useModalFormCrud({
    title: '参数',
    formSchema,
    addApi: addEqpParam,
    editApi: addEqpParam,
    openAfter: (data: Recordable) => {
      const { isUpdate, record } = data;
      if (isUpdate) {
        setFieldsValue({
          chooseEnable: record.chooseEnable.toString(),
        });
      }
    },
    submitAfter: () => {
      emit('success'); // 在这里使用组件的emit
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          tableRef.value?.setData(data);
        },
        validate: () => {
          return tableRef.value?.validate() || Promise.resolve();
        },
        getSubData: () => {
          return tableRef.value?.getData() || {};
        },
      },
    ],
    defaultValues: {
      chooseEnable: '1',
    },
  });
</script>

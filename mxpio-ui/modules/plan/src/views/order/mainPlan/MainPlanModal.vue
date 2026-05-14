<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs v-if="formDataRef?.type === '01'">
      <a-tab-pane key="detailTable" tab="明细" forceRender>
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormSchema } from '@mxpio/components';
  import { mpsSaveApi, mpsChangeApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import DetailTable from './DetailTable.vue';
  import { ref, unref, computed } from 'vue';
  import { duplicateCheck } from '@mxpio/api';

  defineOptions({ name: 'MainPlanModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const isChange = ref(false);
  const formSchema: FormSchema[] = [
    {
      field: 'code',
      label: '编号',
      required: true,
      component: 'Input',
      colProps: {
        span: 6,
      },
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_erp_plan_mps_version',
                column: 'code_',
                key: value,
                exclude: unref(isUpdate) ? value : '',
              })
                .then((res) => {
                  if (res === 1) {
                    return reject('编码已存在');
                  }
                  return resolve();
                })
                .catch((err) => {
                  reject(err.message || '验证失败');
                });
            });
          },
          trigger: 'blur',
        },
      ],
    },
    {
      field: 'name',
      label: '名称',
      required: true,
      component: 'Input',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'bpmnStatus',
      label: '审核状态',
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_COMMON_BPMN_STATUS',
        disabled: true,
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'type',
      label: '计划类别',
      required: true,
      component: 'DictSelect',
      componentProps: {
        dictCode: 'ERP_PLAN_TYPE',
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'rollPeriod',
      label: '滚动周期',
      required: true,
      component: 'InputNumber',
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.type === '02';
      },
    },
    {
      field: 'startDate',
      label: '开始时间',
      required: true,
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
      },
      ifShow: (formData) => {
        return formData.values?.type === '01';
      },
    },
    {
      field: 'endDate',
      label: '结束时间',
      required: true,
      component: 'DatePicker',
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
      },
      colProps: {
        span: 6,
      },
      ifShow: (formData) => {
        return formData.values?.type === '01';
      },
    },
    {
      field: 'changeReason',
      label: '变更事由',
      required: true,
      component: 'InputTextArea',
      colProps: {
        span: 6,
      },
      ifShow: () => {
        return isChange.value;
      },
    },
  ];

  const { registerForm, registerModal, isDisabled, isUpdate, formDataRef, handleSubmit } =
    useModalFormCrud({
      title: '主计划',
      formSchema,
      saveApi: async (data) => {
        return isChange.value ? mpsChangeApi(data) : mpsSaveApi(data);
      },
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      openAfter: (data) => {
        console.log(data);
        isChange.value = data.isChange || false;
      },
      defaultValues: {
        bpmnStatus: '01',
        effectiveStatus: '0',
        type: '01',
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
    });

  const getTitle = computed(() => {
    const baseTitle = unref('主计划') || '';
    if (unref(isDisabled)) return `${baseTitle}查看`;
    return unref(isUpdate)
      ? isChange.value
        ? `${baseTitle}变更`
        : `${baseTitle}编辑`
      : `新增${baseTitle}`;
  });
</script>

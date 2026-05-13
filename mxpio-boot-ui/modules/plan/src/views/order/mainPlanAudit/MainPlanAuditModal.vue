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
    <BasicForm :disabled="true" @register="registerForm" />
    <a-tabs v-if="formDataRef?.type === '01'">
      <a-tab-pane key="detailTable" tab="明细" forceRender>
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
    <BasicForm class="mt-2" :disabled="isDisabled" @register="registerAuditForm" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm, FormSchema, useForm } from '@mxpio/components';
  import { mpsTempAuditApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import DetailTable from './DetailTable.vue';
  import { ref, unref, computed } from 'vue';
  import { duplicateCheck } from '@mxpio/api';
  import { dateUtil } from '@mxpio/utils';
  import { useUserStore } from '@mxpio/stores';

  defineOptions({ name: 'MainPlanAuditModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const isChange = ref(false);
  const id = ref('');
  const userStore = useUserStore();
  const { username } = userStore.getUserInfo || {};
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
      componentProps: {
        disabled: true,
      },
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
        disabled: true,
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
      componentProps: {
        disabled: true,
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
        disabled: true,
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
        disabled: true,
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
      componentProps: {
        disabled: true,
      },
      ifShow: () => {
        return isChange.value;
      },
    },
  ];

  const formAuditSchema: FormSchema[] = [
    {
      field: 'agree',
      label: '是否同意',
      component: 'DictSelect',
      required: true,
      componentProps: {
        dictCode: 'MB_SYSTEM_YES_NO',
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'auditOpinion',
      label: '审核意见',
      required: true,
      component: 'InputTextArea',
      colProps: {
        span: 6,
      },
    },
    {
      field: 'reviewer',
      label: '审核人',
      component: 'UserByDeptSelect',
      componentProps: {
        disabled: true,
      },
      colProps: {
        span: 6,
      },
    },
    {
      field: 'reviewTime',
      label: '审核时间',
      component: 'DatePicker',
      colProps: {
        span: 6,
      },
      componentProps: {
        valueFormat: 'YYYY-MM-DD',
        class: '!w-full',
        disabled: true,
      },
    },
  ];

  const [registerAuditForm, { setFieldsValue, validate, getFieldsValue }] = useForm({
    labelWidth: 100,
    baseColProps: { span: 24 },
    schemas: formAuditSchema,
    showActionButtonGroup: false,
  });

  const {
    registerForm,
    registerModal,
    isDisabled,
    isUpdate,
    formDataRef,
    setModalProps,
    closeModal,
  } = useModalFormCrud({
    title: '主计划审核',
    formSchema,
    openAfter: (data: Recordable) => {
      const record = data.record || {};
      id.value = record.id || '';
      if (isDisabled) {
        setFieldsValue({
          agree: record.bpmnStatus === '03' ? '1' : record.bpmnStatus === '99' ? '0' : '',
          reviewTime: record.reviewTime,
          reviewer: record.username,
          auditOpinion: record.reviewOpinion,
        });
      } else {
        setFieldsValue({
          agree: '1',
          reviewTime: dateUtil().format('YYYY-MM-DD'),
          reviewer: username,
        });
      }
    },
    subTables: [
      {
        initSubData: (data: Recordable) => {
          tableRef.value?.setData(data);
        },
      },
    ],
  });

  const getTitle = computed(() => (!unref(isDisabled) ? '主计划审核' : '主计划审核详情'));

  async function handleSubmit() {
    try {
      await validate();
      setModalProps({ confirmLoading: true });
      const values = getFieldsValue();
      await mpsTempAuditApi(id.value, values);
      closeModal();
      emit('success');
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }
</script>

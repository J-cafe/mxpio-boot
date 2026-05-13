<template>
  <BasicModal
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="1" tab="工序信息">
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, unref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import type { FormSchema } from '@mxpio/components';
  import { saveProcStandrout } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import DetailTable from './DetailTable.vue';
  import { duplicateCheck } from '@mxpio/api';

  defineOptions({ name: 'ProcStandroutModal' });

  const emit = defineEmits(['success', 'register']);
  const formSchema: FormSchema[] = [
    {
      field: 'routCode',
      label: '工艺路线编码',
      component: 'Input',
      colProps: {
        span: 8,
      },
      required: true,
      rules: [
        {
          // @ts-ignore
          validator: async (rule, value) => {
            return new Promise((resolve, reject) => {
              if (!value || unref(isUpdate)) return resolve();
              duplicateCheck({
                tableName: 'mb_erp_technology_stand_rout',
                column: 'rout_code_',
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
      field: 'routName',
      label: '工艺路线名称',
      component: 'Input',
      required: true,
      colProps: {
        span: 8,
      },
    },
    {
      field: 'routDesc',
      label: '工艺路线说明',
      component: 'InputTextArea',
      colProps: {
        span: 8,
      },
    },
  ];

  const tableRef = ref<InstanceType<typeof DetailTable>>();
  const { registerForm, registerModal, getTitle, isDisabled, isUpdate, handleSubmit } =
    useModalFormCrud({
      title: '标准工艺路线',
      formSchema,
      saveApi: saveProcStandrout,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        status: '1',
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
</script>

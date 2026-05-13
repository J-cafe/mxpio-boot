<template>
  <BasicModal
    v-bind="$attrs"
    width="80%"
    destroyOnClose
    @register="registerModal"
    :title="getTitle"
    @ok="handleSubmit"
    :showOkBtn="!isDisabled"
  >
    <BasicForm :disabled="isDisabled" @register="registerForm" />
    <a-tabs>
      <a-tab-pane key="detailTable" tab="检测项目" forceRender>
        <DetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { saveQtApi } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { formSchema } from './template.data';
  import DetailTable from './DetailTable.vue';
  import { ref } from 'vue';

  defineOptions({ name: 'QualityTemplateModal' });

  const emit = defineEmits(['success', 'register']);
  const tableRef = ref<InstanceType<typeof DetailTable>>();

  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit, formDataRef } =
    useModalFormCrud({
      title: '检测方案',
      formSchema,
      saveApi: saveQtApi,
      submitAfter: () => {
        emit('success'); // 在这里使用组件的emit
      },
      defaultValues: {
        bpmnStatus: '01',
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
      submitBefore: () => {
        if (formDataRef.itemType !== '2' && formDataRef.itemType !== 2) {
          return Promise.resolve();
        }
        const { fullData = [] } = tableRef.value?.getTableData() || {};
        if (fullData.length <= 1) {
          return Promise.reject({ message: '请填写至少两条检测选项' });
        }
        return Promise.resolve();
      },
    });
</script>

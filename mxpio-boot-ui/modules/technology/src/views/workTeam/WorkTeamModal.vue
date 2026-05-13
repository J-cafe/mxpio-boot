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
      <a-tab-pane key="1" tab="班组成员">
        <WorkTeamDetailTable ref="tableRef" />
      </a-tab-pane>
    </a-tabs>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, BasicForm } from '@mxpio/components';
  import { saveWorkteam } from '@mxpio/bizcommon';
  import { useModalFormCrud } from '@mxpio/common';
  import { Tabs as ATabs } from 'ant-design-vue';
  import WorkTeamDetailTable from './WorkTeamDetailTable.vue';
  import { formSchema } from './workTeam.data';

  defineOptions({ name: 'WorkTeamModal' });

  const emit = defineEmits(['success', 'register']);

  const tableRef = ref<InstanceType<typeof WorkTeamDetailTable>>();
  const { registerForm, registerModal, getTitle, isDisabled, handleSubmit } = useModalFormCrud({
    title: '班组',
    formSchema,
    saveApi: saveWorkteam,
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
  });
</script>

<template>
  <BasicModal
    :title="title"
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    :showOkBtn="false"
    @register="registerModal"
    @cancel="handleCancel"
  >
    <VFormCreate
      ref="vFormNode"
      v-if="showNodeBpmn"
      :formConfig="nodeFormConfigDef"
      v-model:formModel="nodeFormData"
    />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner, VFormCreate } from '@mxpio/components';
  // import VFormCreate from '../../components/form-design/components/VFormCreate/index.vue';
  import { getFormModel } from '@mxpio/bizcommon';

  defineOptions({ name: 'HistoryFormModalModal' });

  const nodeFormData = ref({}); //节点表单数据

  const nodeFormConfigDef = ref(); //发起表单配置信息

  const showNodeBpmn = ref(false);
  const title = ref('节点表单详情');

  const [registerModal, { setModalProps, closeModal }] = useModalInner(
    async ({ formData, record }) => {
      setModalProps({ confirmLoading: false });
      getFormModelById(record.id);
      title.value = `节点【${record.name}】表单详情`;
      nodeFormData.value = formData;
    },
  );

  function handleCancel() {
    closeModal();
  }

  async function getFormModelById(id: string) {
    try {
      //获取节点表单数据
      const res = await getFormModel(id);
      nodeFormConfigDef.value = res.model ? JSON.parse(res.model) : {};
      nodeFormConfigDef.value.disabled = true;
      showNodeBpmn.value = true;
    } catch (error) {
      console.log('error', error);
    }
  }
</script>

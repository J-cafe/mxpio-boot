<template>
  <BasicModal
    width="80%"
    v-bind="$attrs"
    destroyOnClose
    @register="registerModal"
    title="多阶BOM"
    cancelText="关闭"
    :showOkBtn="false"
  >
    <BomStoreyTree :parentCode="currentRow.parentCode" :parentData="currentRow" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { BasicModal, useModalInner } from '@mxpio/components';
  import BomStoreyTree from './BomStoreyTree.vue';

  defineOptions({ name: 'BomStoreyTreeModal' });
  const currentRow = ref<Recordable>({});

  const [registerModal, { setModalProps }] = useModalInner(async (data) => {
    setModalProps({ confirmLoading: false });
    currentRow.value = data.record;
  });
</script>

<template>
  <BasicModal @register="registerModal" title="Cron表达式" width="800px" @ok="onOk">
    <EasyCron v-bind="$attrs" />
  </BasicModal>
</template>

<script lang="ts">
  import { defineComponent } from 'vue';
  import { useAttrs } from '@mxpio/hooks/src/core/useAttrs';
  import { BasicModal, useModalInner } from '../../../../Modal';
  import EasyCron from './EasyCronInner.vue';

  export default defineComponent({
    name: 'EasyCronModal',
    components: { BasicModal, EasyCron },
    inheritAttrs: false,
    setup() {
      const attrs = useAttrs();
      const [registerModal, { closeModal }] = useModalInner();

      function onOk() {
        closeModal();
      }

      return { attrs, registerModal, onOk };
    },
  });
</script>

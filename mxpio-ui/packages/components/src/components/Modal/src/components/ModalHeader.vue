<template>
  <BasicTitle :helpMessage="helpMessage">
    {{ title }}
  </BasicTitle>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import { BasicTitle } from '../../../Basic';

  defineOptions({ name: 'BasicModalHeader' });

  defineProps({
    helpMessage: {
      type: [String, Array] as PropType<string | string[]>,
    },
    title: { type: String },
  });
</script>

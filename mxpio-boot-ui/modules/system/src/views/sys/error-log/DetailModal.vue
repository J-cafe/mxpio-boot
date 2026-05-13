<template>
  <BasicModal :width="800" :title="$t('sys.errorLog.tableActionDesc')" v-bind="$attrs">
    <Description :data="info" @register="register" />
  </BasicModal>
</template>
<script lang="ts" setup>
  import type { PropType } from 'vue';
  import type { ErrorLogInfo } from '@mxpio/types';
  import { BasicModal, Description, useDescription } from '@mxpio/components';
  import { $t } from '@mxpio/locales';
  import { getDescSchema } from './data';

  defineProps({
    info: {
      type: Object as PropType<ErrorLogInfo>,
      default: null,
    },
  });

  const [register] = useDescription({
    column: 2,
    schema: getDescSchema()!,
  });
</script>

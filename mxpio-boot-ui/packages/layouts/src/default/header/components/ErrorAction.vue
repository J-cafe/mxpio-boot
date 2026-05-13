<template>
  <Tooltip
    :title="$t('layout.header.tooltipErrorLog')"
    placement="bottom"
    :mouseEnterDelay="0.5"
    @click="handleToErrorList"
  >
    <Badge :count="getCount" :offset="[0, 10]" :overflowCount="99">
      <Icon icon="ion:bug-outline" />
    </Badge>
  </Tooltip>
</template>
<script lang="ts" setup>
  import { computed } from 'vue';
  import { Tooltip, Badge } from 'ant-design-vue';
  import { Icon } from '@mxpio/components';
  import { $t } from '@mxpio/locales';
  // import { useStore } from '@mxpio/bridge';
  import { useErrorLogStore } from '@mxpio/stores';
  import { PageEnum } from '@mxpio/enums/src/pageEnum';

  import { useRouter } from 'vue-router';

  defineOptions({ name: 'ErrorAction' });

  const { push } = useRouter();
  // const useErrorLogStore = useStore('useErrorLogStore');
  const errorLogStore = useErrorLogStore();

  const getCount = computed(() => errorLogStore.getErrorLogListCount);

  function handleToErrorList() {
    push(PageEnum.ERROR_LOG_PAGE).then(() => {
      errorLogStore.setErrorLogListCount(0);
    });
  }
</script>

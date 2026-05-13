<template>
  <div v-if="showFrame">
    <template v-for="frame in getFramePages" :key="frame.path">
      <FramePage
        v-if="frame.meta.frameSrc && hasRenderFrame(frame.name) && !frame.meta.outside"
        v-show="showIframe(frame)"
        :frameSrc="frame.meta.frameSrc"
      />
    </template>
  </div>
</template>
<script lang="ts" setup>
  import { unref, computed } from 'vue';
  import FramePage from './FramePage.vue';

  import { useFrameKeepAlive } from './useFrameKeepAlive';

  defineOptions({ name: 'FrameLayout' });

  const { getFramePages, hasRenderFrame, showIframe } = useFrameKeepAlive();

  const showFrame = computed(() => unref(getFramePages).length > 0);
</script>

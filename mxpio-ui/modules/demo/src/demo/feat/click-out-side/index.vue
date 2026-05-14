<template>
  <PageWrapper title="点内外部触发事件">
    <ClickOutSide @click-outside="handleClickOutside" class="flex justify-center">
      <div
        @click="innerClick"
        class="flex items-center justify-center w-full h-300px border-10px bg-blue-500 text-white text-24px"
      >
        {{ text }}
      </div>
    </ClickOutSide>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { ref } from 'vue';
  import { ClickOutSide, PageWrapper } from '@mxpio/components';

  const text = ref('Click');

  function handleClickOutside() {
    text.value = 'Click Out Side';
  }

  function innerClick() {
    text.value = 'Click Inner';
  }
</script>

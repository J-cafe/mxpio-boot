<template>
  <PageWrapper title="引导页" content="用于给用户的指引操作">
    <a-button type="primary" @click="handleStart">开始</a-button>
  </PageWrapper>
</template>
<script lang="ts" setup>
  import { PageWrapper } from '@mxpio/components';
  import { useDesign } from '@mxpio/hooks/src/web/useDesign';
  import { driver } from 'driver.js';
  import 'driver.js/dist/driver.css';

  const { prefixVar } = useDesign('');

  function handleStart() {
    driver({
      showProgress: true,
      steps: [
        {
          popover: {
            title: 'Welcome',
            description: 'Hello World! 👋',
          },
        },
        {
          element: `.${prefixVar}-layout-header-trigger`,
          popover: {
            title: 'Collapse Button',
            description: 'This is the menu collapse button.',
          },
        },
        {
          element: `.${prefixVar}-layout-header-action`,
          popover: {
            title: 'User Action',
            description: 'This is the user function area.',
          },
        },
      ],
    }).drive();
  }
</script>

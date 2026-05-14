<template>
  <a-button @click="handleClick" shape="circle">
    <Icon icon="ant-design:upload-outlined" />
  </a-button>
</template>

<script lang="ts" setup>
  import type { VxeGridInstance } from '@mxpio/types';
  import { Icon } from '@mxpio/components';
  import { message } from 'ant-design-vue';
  import { importUpload } from '@mxpio/api';
  import { propTypes } from '@mxpio/utils/src/propTypes';

  defineOptions({ name: 'ImportButton' });

  const props = defineProps({
    importCode: propTypes.oneOfType([propTypes.string, propTypes.func]),
    params: {
      type: Object,
      required: false,
    },
    events: {
      type: Object,
      required: false,
    },
  });

  // 点击导出
  async function handleClick() {
    try {
      // 判断自定义click事件时，默认的不执行
      if (props.events?.onClick) {
        return false;
      }
      const $grid = props.params?.$grid as VxeGridInstance<Recordable>;
      if ($grid) {
        $grid.openImport({
          importMethod: ({ file }) => {
            return handleImport(file);
          },
        });
      }
    } catch (e) {
      console.error(e);
    }
  }

  async function handleImport(file: File) {
    try {
      const formData = new FormData();
      formData.append('file', file);
      let importCode = props.importCode;
      if (typeof props.importCode === 'function') {
        importCode = await props.importCode();
      }
      const res = await importUpload(importCode, {
        file: file,
      });
      if (res.data.success) {
        message.success(res.data?.message);
        const $grid = props.params?.$grid as VxeGridInstance<Recordable>;
        $grid?.commitProxy('query');
      } else {
        message.error(res.data?.message);
      }
    } catch {
      message.error('导入失败');
    }
  }
</script>

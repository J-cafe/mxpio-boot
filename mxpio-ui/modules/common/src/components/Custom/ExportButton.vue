<template>
  <a-dropdown-button type="primary" :disabled="exportTemplateList.length === 0">
    <template #overlay>
      <a-menu>
        <a-menu-item v-for="(item, i) in exportTemplateList" :key="i" @click="handleClick(item)">
          {{ item.fileName }}
        </a-menu-item>
      </a-menu>
    </template>
    <Icon icon="ant-design:download-outlined" />
    导出
  </a-dropdown-button>
</template>

<script lang="ts" setup>
  import { ref, onMounted, Ref } from 'vue';
  import { DropdownButton as ADropdownButton, message } from 'ant-design-vue';
  import { getQueryParams } from '@mxpio/utils/src/criteria';
  import { exportList, exportDownload } from '@mxpio/api';
  import { Icon } from '@mxpio/components';
  import { downloadByData } from '@mxpio/utils/src/file/download';

  defineOptions({ name: 'ExportButton' });

  const props = defineProps({
    export: {
      type: [Number, String],
      required: true,
    },
    getParams: {
      //获取导出接口参数
      type: Function,
      required: false,
    },
  });

  const exportTemplateList: Ref<Recordable[]> = ref([]);

  // 初始化导出模板列表数据
  async function initExportTemplate() {
    const params = getQueryParams({
      page: 1,
      size: 1000,
      [`elementId`]: props.export,
    });
    const res = await exportList(params);
    exportTemplateList.value = res.content;
  }

  // 点击导出
  async function handleClick(item: Recordable) {
    try {
      message.loading({ content: '下载中...', duration: 0 });
      let params = {};
      if (props.getParams) {
        params = await props.getParams();
      }
      const res = await exportDownload(item.code, 'xls', params);
      message.destroy();
      const { data, headers } = res;
      let fileName = decodeURIComponent(
        headers['content-disposition'].split(';')[1].split('filename=')[1],
      );
      // fileName字符串带""时，截取名称
      if (fileName.indexOf('"') === 0) {
        fileName = fileName.slice(1, fileName.length - 1);
      }
      downloadByData(data, fileName);
    } catch (e) {
      console.error(e);
      message.destroy();
    }
  }

  onMounted(() => {
    initExportTemplate();
  });
</script>

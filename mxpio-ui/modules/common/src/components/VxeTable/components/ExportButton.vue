<template>
  <a-dropdown :disabled="exportTemplateList.length <= 0">
    <template #overlay>
      <a-menu>
        <a-menu-item v-for="(item, i) in exportTemplateList" :key="i" @click="handleClick(item)">
          {{ item.fileName }}
        </a-menu-item>
      </a-menu>
    </template>
    <a-button :disabled="exportTemplateList.length <= 0" shape="circle">
      <Icon icon="ant-design:download-outlined" />
    </a-button>
  </a-dropdown>
</template>

<script lang="ts" setup>
  import { ref, onMounted, Ref } from 'vue';
  import { getQueryParams, getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { exportList, exportDownload } from '@mxpio/api';
  import { Icon } from '@mxpio/components';
  import { Dropdown as ADropdown, message } from 'ant-design-vue';
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
    params: {
      type: Object,
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
      } else {
        // 组装参数
        params = getParams_();
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

  function getParams_() {
    // 获取搜索条件
    const proxyInfo = props.params?.$grid?.getProxyInfo();
    // 组装参数
    const params = getVxeTableQueryParams({
      form: proxyInfo?.form,
      page: {
        currentPage: 1,
        pageSize: (proxyInfo?.pager?.total || 0) + 9999,
        total: proxyInfo?.pager.total || 0,
      },
    });
    return params;
  }

  onMounted(() => {
    initExportTemplate();
  });
</script>

<template>
  <Result
    status="success"
    class="bg-white h-full"
    :title="`已打开外部链接：${currentRoute.meta.frameSrc}`"
  >
    <template #extra>
      <a-button key="open" @click="goUrl" type="primary">
        <template #icon>
          <ExportOutlined />
        </template>
        打开链接</a-button
      >
      <a-button key="close" @click="closeWindow">关闭窗口</a-button>
    </template>
  </Result>
</template>
<script lang="ts" setup>
  import { Button, Result } from 'ant-design-vue';
  import { isHttpUrl, getToken } from '@mxpio/utils';
  import { useRouter } from 'vue-router';
  import { onMounted } from 'vue';
  import { ExportOutlined } from '@ant-design/icons-vue';
  import { useTabs } from '@mxpio/hooks/src/web/useTabs';

  const AButton = Button;
  const { currentRoute } = useRouter();
  const { closeCurrent } = useTabs();
  function goUrl() {
    const link = currentRoute.value?.meta?.frameSrc || '';
    const query = currentRoute.value?.query || {};
    gotoFullPage(link, query);
  }

  function closeWindow() {
    closeCurrent();
  }

  function gotoFullPage(path: string, query: Record<string, any>) {
    let url = '';
    if (isHttpUrl(path)) {
      url = path;
    } else {
      url = `${window.location.origin}${path}`;
    }
    const tokenStr = '${token}';
    if (url.indexOf(tokenStr) !== -1) {
      const token = getToken() as string | null | undefined;
      url = url.replace(tokenStr, token || '');
    }
    url = setUrlParams(url, query);
    window.open(url, '_blank');
  }

  function setUrlParams(url: string, obj: Record<string, any>) {
    const params = new URLSearchParams();
    Object.keys(obj).forEach((key) => {
      params.append(key, String(obj[key]));
    });
    if (params.toString()) {
      url += (url.includes('?') ? '&' : '?') + params.toString();
    }
    return url;
  }

  onMounted(() => {
    goUrl();
  });
</script>
<style lang="less"></style>

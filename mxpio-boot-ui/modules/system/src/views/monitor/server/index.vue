<template>
  <div class="p-4">
    <a-card :bordered="false" style="height: 100%">
      <a-tabs v-model:activeKey="activeKey" @change="tabChange">
        <a-tab-pane key="1" tab="服务器信息" />
        <a-tab-pane key="2" tab="JVM信息" force-render />
        <a-tab-pane key="3" tab="Tomcat信息" />
      </a-tabs>
      <BasicTable @register="registerTable" :searchInfo="searchInfo" :dataSource="dataSource">
        <template #tableTitle>
          <div>
            上次更新时间：{{ lastUpdateTime }}
            <a-divider type="vertical" />
            <a @click="handleUpdate">立即更新</a></div
          >
        </template>
        <template #param="{ record, text }">
          <a-tag :color="textInfo[record.param].color">{{ text }}</a-tag>
        </template>
        <template #text="{ record }">
          {{ textInfo[record.param].text }}
        </template>
        <template #value="{ record, text }">
          {{ text }} {{ textInfo[record.param].unit }}
        </template>
      </BasicTable>
    </a-card>
  </div>
</template>
<script lang="ts" name="monitor-server" setup>
  import { onMounted, ref, unref } from 'vue';
  import { Card as ACard, Tabs as ATabs, Tag as ATag, Divider as ADivider } from 'ant-design-vue';
  import { BasicTable, useTable } from '@mxpio/components';
  import { getServerInfo, getTextInfo, getMoreInfo } from './server.api';
  import { columns } from './server.data';
  // import dayjs from 'dayjs';
  import { dateUtil } from '@mxpio/utils';

  defineOptions({ name: 'SystemInfo' });
  // 添加接口定义
  interface ServerInfoItem {
    name: string;
    measurements: Array<{ value: any }>;
  }

  interface TextInfoItem {
    color: string;
    valueType: 'Number' | 'Date' | 'RAM' | string;
    unit: string;
    text: string;
  }

  interface DataInfo {
    id: string;
    param: string;
    text: string;
    value: any;
  }

  const ATabPane = ATabs.TabPane;
  const dataSource = ref<DataInfo[]>([]);
  const activeKey = ref('1');
  const moreInfo = ref<Record<string, string[]>>({});
  const lastUpdateTime = ref({});
  let textInfo = ref<Record<string, TextInfoItem>>({});

  const searchInfo = { logType: '1' };
  const [registerTable] = useTable({
    columns,
    showIndexColumn: false,
    bordered: true,
    pagination: false,
    canResize: false,
    tableSetting: { fullScreen: true },
    rowKey: 'id',
  });

  //tab切换
  function tabChange(key) {
    if (key != 4) {
      getInfoList(key);
    }
  }

  //加载信息
  function getInfoList(infoType: string) {
    lastUpdateTime.value = dateUtil().format('YYYY年MM月DD日 HH时mm分ss秒');
    getServerInfo(infoType)?.then((res: ServerInfoItem[]) => {
      textInfo.value = getTextInfo(infoType);
      moreInfo.value = getMoreInfo(infoType);
      let info: Array<DataInfo> = [];
      res.forEach((value, id) => {
        let more = unref(moreInfo)[value.name];
        if (!(more instanceof Array)) {
          more = [''];
        }
        more.forEach((item, idx) => {
          let param = value.name + item;
          let val = convert(value.measurements[idx].value, unref(textInfo)[param].valueType);
          info.push({ id: param + id, param, text: 'false value', value: val });
        });
      });
      dataSource.value = info;
    });
  }

  function handleUpdate() {
    getInfoList(activeKey.value);
  }

  //单位转换
  function convert(value, type) {
    if (type === 'Number') {
      return Number(value * 100).toFixed(2);
    } else if (type === 'Date') {
      return dateUtil(value * 1000).format('YYYY-MM-DD HH:mm:ss');
    } else if (type === 'RAM') {
      return Number(value / 1048576).toFixed(3);
    }
    return value;
  }

  onMounted(() => {
    getInfoList(activeKey.value);
  });
</script>

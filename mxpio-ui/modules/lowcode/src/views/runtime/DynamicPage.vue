<template>
  <div class="lowcode-dynamic-page">
    <Spin :spinning="pageLoading">
      <!-- 按照layoutConfig渲染布局 -->
      <template v-if="pageConfig">
        <a-row v-for="(row, ri) in layoutRows" :key="ri" :gutter="16">
          <a-col
            v-for="(col, ci) in row.cols"
            :key="ci"
            :span="col.span || 24"
          >
            <!-- 根据widget类型渲染对应控件 -->
            <div class="widget-wrapper">
              <!-- DataPilot -->
              <DataPilot
                v-if="col.widget?.type === 'DataPilot'"
                :dataSet="getDataSet(col.widget.dataSet)"
              />

              <!-- DataGrid -->
              <DataGridWidget
                v-if="resolveWidget(col.widget) === 'DataGrid'"
                :dataSet="getDataSet(col.widget.dataSet)"
                :columns="col.widget.columns || getDefaultColumns(col.widget.dataSet)"
              />

              <!-- AutoForm -->
              <AutoFormWidget
                v-if="resolveWidget(col.widget) === 'AutoForm'"
                :dataSet="getDataSet(col.widget.dataSet)"
                :fields="getFormFields(col.widget.dataSet)"
              />
            </div>
          </a-col>
        </a-row>
      </template>
      <a-empty v-else description="页面配置为空" />
    </Spin>
  </div>
</template>

<script lang="ts" setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { Spin, Row, Col, Empty } from 'ant-design-vue';
import { useBridge } from '@mxpio/bridge';
import { DataSet } from '../../core/DataSet';
import DataGridWidget from '../../components/DataGridWidget.vue';
import AutoFormWidget from '../../components/AutoFormWidget.vue';
import DataPilot from '../../components/DataPilot.vue';

interface WidgetConfig {
  id: string;
  type: string;
  dataSet?: string;
  dataPath?: string;
  columns?: Array<{ field: string; title: string; width?: number }>;
  colProps?: { span?: number };
}

interface LayoutRow {
  cols: Array<{
    span: number;
    widget: WidgetConfig;
  }>;
}

interface PageConfig {
  pageCode: string;
  pageName: string;
  modelCode: string;
  widgetConfig: {
    widgets: WidgetConfig[];
    layout: {
      rows: Array<{
        cols: Array<{ span: number; widget: string }>;
      }>;
    };
  };
}

const props = defineProps<{
  pageCode: string;
}>();

const pageLoading = ref(false);
const pageConfig = ref<PageConfig | null>(null);

// DataSet 实例池
const dataSets = reactive<Map<string, DataSet>>(new Map());

const layoutRows = computed<LayoutRow[]>(() => {
  if (!pageConfig.value) return [];
  const { widgets, layout } = pageConfig.value.widgetConfig;
  const widgetMap = new Map<string, WidgetConfig>();
  widgets.forEach((w) => widgetMap.set(w.id, w));

  return (layout.rows || []).map((row) => ({
    cols: row.cols.map((col) => ({
      span: col.span || 24,
      widget: widgetMap.get(col.widget) || ({} as WidgetConfig),
    })),
  }));
});

function getDataSet(dsId: string): DataSet {
  if (!dataSets.has(dsId)) {
    const ds = new DataSet({
      id: dsId,
      modelCode: pageConfig.value?.modelCode || '',
    });
    dataSets.set(dsId, ds);
  }
  return dataSets.get(dsId)!;
}

function resolveWidget(widget: WidgetConfig): string {
  return widget?.type || '';
}

function getDefaultColumns(dsId: string): any[] {
  return [
    { field: 'id', title: 'ID', width: 150 },
    { field: 'name', title: '名称', width: 200 },
  ];
}

function getFormFields(_dsId: string): any[] {
  return [];
}

// 加载页面配置
async function loadPageConfig() {
  pageLoading.value = true;
  try {
    const { http } = useBridge();
    const res: any = await http.get({
      url: `/lowcode/page/${props.pageCode}`,
    });
    pageConfig.value = res;
    // 初始化所有 DataSet 并加载数据
    await initDataSets();
  } catch (e: any) {
    console.error('加载页面配置失败:', e);
  } finally {
    pageLoading.value = false;
  }
}

async function initDataSets() {
  const widgets = pageConfig.value?.widgetConfig?.widgets || [];
  for (const w of widgets) {
    if (w.dataSet) {
      const ds = getDataSet(w.dataSet);
      await ds.load();
    }
  }
}

onMounted(() => {
  if (props.pageCode) {
    loadPageConfig();
  }
});
</script>

<style scoped>
.lowcode-dynamic-page {
  padding: 12px;
  min-height: 400px;
}
.widget-wrapper {
  margin-bottom: 12px;
}
</style>

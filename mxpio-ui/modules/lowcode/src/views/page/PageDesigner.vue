<template>
  <div class="page-designer">
    <!-- 顶部工具栏 -->
    <div class="designer-toolbar">
      <a-space>
        <a-input
          v-model:value="pageName"
          placeholder="页面名称"
          style="width: 200px"
        />
        <a-select
          v-model:value="modelCode"
          placeholder="选择数据模型"
          style="width: 200px"
          :options="modelOptions"
        />
        <a-button type="primary" @click="handleSave">保存</a-button>
        <a-button @click="handlePreview">预览</a-button>
        <a-button @click="handlePublish">发布</a-button>
      </a-space>
      <span class="toolbar-hint">从左侧拖拽组件到画布中 →</span>
    </div>

    <!-- 三栏布局 -->
    <div class="designer-body">
      <div class="panel-left">
        <WidgetPanel @dragStart="onDragStart" />
      </div>
      <div class="panel-center">
        <DesignCanvas
          :rows="canvasRows"
          :selectedId="selectedWidgetId"
          @update:rows="onRowsUpdate"
          @select="onSelectWidget"
        />
      </div>
      <div class="panel-right">
        <PropEditor
          :widget="selectedWidget"
          @update="onWidgetUpdate"
        />
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref, computed, onMounted } from 'vue';
import { message, Space, Input, Select, Button } from 'ant-design-vue';
import { useRoute, useRouter } from 'vue-router';
import { useBridge } from '@mxpio/bridge';
import WidgetPanel from '../../components/WidgetPanel.vue';
import DesignCanvas from '../../components/DesignCanvas.vue';
import PropEditor from '../../components/PropEditor.vue';
import type { CanvasRow, CanvasWidget } from '../../components/DesignCanvas.vue';

const route = useRoute();
const router = useRouter();
const { http } = useBridge();

const pageName = ref('');
const modelCode = ref<string | undefined>();
const modelOptions = ref<Array<{ label: string; value: string }>>([]);

// 画布状态
const canvasRows = ref<CanvasRow[]>([]);
const selectedWidgetId = ref<string | null>(null);
const pageCode = ref('');

const selectedWidget = computed(() => {
  if (!selectedWidgetId.value) return null;
  for (const row of canvasRows.value) {
    for (const col of row.cols) {
      if (col.widget?.id === selectedWidgetId.value) return col.widget;
    }
  }
  return null;
});

function onDragStart() {
  // 拖拽开始
}

function onRowsUpdate(rows: CanvasRow[]) {
  canvasRows.value = rows;
}

function onSelectWidget(id: string | null) {
  selectedWidgetId.value = id;
}

function onWidgetUpdate(widget: CanvasWidget) {
  for (const row of canvasRows.value) {
    for (const col of row.cols) {
      if (col.widget?.id === widget.id) {
        col.widget = widget;
        return;
      }
    }
  }
}

// 构建 widgetConfig JSON
function buildWidgetConfig(): string {
  const widgets: any[] = [];
  for (const row of canvasRows.value) {
    for (const col of row.cols) {
      if (col.widget) widgets.push(col.widget);
    }
  }
  const layout = {
    rows: canvasRows.value.map((row) => ({
      cols: row.cols.map((col) => ({
        span: col.span,
        widget: col.widget?.id || null,
      })),
    })),
  };
  return JSON.stringify({ widgets, layout });
}

async function handleSave() {
  if (!pageName.value || !modelCode.value) {
    message.warning('请填写页面名称并选择数据模型');
    return;
  }
  const widgetConfig = buildWidgetConfig();
  try {
    if (pageCode.value) {
      await http.put({
        url: `/lowcode/page/${pageCode.value}`,
        data: {
          pageName: pageName.value,
          modelCode: modelCode.value,
          pageType: 'LIST',
          widgetConfig,
        },
      });
    } else {
      const code = '/lowcode/' + Date.now();
      pageCode.value = code;
      await http.post({
        url: '/lowcode/page',
        data: {
          pageCode: code,
          pageName: pageName.value,
          modelCode: modelCode.value,
          pageType: 'LIST',
          widgetConfig,
        },
      });
    }
    message.success('保存成功');
  } catch (e: any) {
    message.error('保存失败: ' + (e?.message || ''));
  }
}

function handlePreview() {
  if (pageCode.value) {
    router.push(`/lowcode/preview/${pageCode.value}`);
  }
}

async function handlePublish() {
  if (!pageCode.value) {
    message.warning('请先保存页面');
    return;
  }
  try {
    await http.post({ url: `/lowcode/page/${pageCode.value}/publish` });
    message.success('发布成功');
  } catch (e: any) {
    message.error('发布失败: ' + (e?.message || ''));
  }
}

// 加载数据模型列表
async function loadModels() {
  try {
    const res: any = await http.get({ url: '/lowcode/model/list' });
    modelOptions.value = (res || []).map((m: any) => ({
      label: `${m.modelName} (${m.modelCode})`,
      value: m.modelCode,
    }));
  } catch (e) {
    // 忽略
  }
}

// 如果是编辑已有页面
async function loadPage() {
  const code = route.query.pageCode as string;
  if (!code) return;
  try {
    const res: any = await http.get({ url: `/lowcode/page/${code}` });
    if (res) {
      pageCode.value = res.pageCode;
      pageName.value = res.pageName;
      modelCode.value = res.modelCode;
      if (res.widgetConfig) {
        const config = JSON.parse(res.widgetConfig);
        canvasRows.value = config.layout?.rows?.map((row: any) => ({
          cols: (row.cols || []).map((col: any) => ({
            span: col.span || 24,
            widget: config.widgets?.find((w: any) => w.id === col.widget) || null,
          })),
        })) || [];
      }
    }
  } catch (e) {
    // 新建页面
  }
}

onMounted(() => {
  loadModels();
  loadPage();
});
</script>

<style scoped>
.page-designer {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background: #f0f2f5;
}
.designer-toolbar {
  padding: 8px 16px;
  background: #fff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
}
.toolbar-hint {
  color: #999;
  font-size: 12px;
}
.designer-body {
  flex: 1;
  display: flex;
  overflow: hidden;
}
.panel-left {
  width: 220px;
  flex-shrink: 0;
  overflow-y: auto;
  background: #fff;
}
.panel-center {
  flex: 1;
  overflow-y: auto;
}
.panel-right {
  width: 280px;
  flex-shrink: 0;
  overflow-y: auto;
  background: #fff;
}
</style>

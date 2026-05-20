<template>
  <div class="widget-panel">
    <div class="panel-title">组件面板</div>
    <a-collapse v-model:activeKey="activeKeys">
      <a-collapse-panel key="layout" header="布局组件">
        <div class="widget-list">
          <div
            v-for="w in layoutWidgets"
            :key="w.type"
            class="widget-item"
            draggable="true"
            @dragstart="onDragStart($event, w)"
          >
            <component :is="w.icon" v-if="w.icon" />
            <span>{{ w.label }}</span>
          </div>
        </div>
      </a-collapse-panel>

      <a-collapse-panel key="data" header="数据组件">
        <div class="widget-list">
          <div
            v-for="w in dataWidgets"
            :key="w.type"
            class="widget-item"
            draggable="true"
            @dragstart="onDragStart($event, w)"
          >
            <span>{{ w.label }}</span>
          </div>
        </div>
      </a-collapse-panel>

      <a-collapse-panel key="action" header="操作组件">
        <div class="widget-list">
          <div
            v-for="w in actionWidgets"
            :key="w.type"
            class="widget-item"
            draggable="true"
            @dragstart="onDragStart($event, w)"
          >
            <span>{{ w.label }}</span>
          </div>
        </div>
      </a-collapse-panel>
    </a-collapse>
  </div>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { Collapse, CollapsePanel } from 'ant-design-vue';

export interface WidgetDef {
  type: string;
  label: string;
  icon?: string;
  defaultProps?: Record<string, any>;
}

const layoutWidgets: WidgetDef[] = [
  { type: 'Row', label: '行容器', defaultProps: { cols: [{ span: 24 }] } },
  { type: 'Col', label: '列', defaultProps: { span: 12 } },
];

const dataWidgets: WidgetDef[] = [
  { type: 'DataGrid', label: '数据表格' },
  { type: 'AutoForm', label: '自动表单' },
];

const actionWidgets: WidgetDef[] = [
  { type: 'DataPilot', label: 'CRUD导航条' },
  { type: 'Button', label: '按钮' },
];

const activeKeys = ref(['data', 'action']);

const emit = defineEmits<{
  (e: 'dragStart', widget: WidgetDef): void;
}>();

function onDragStart(event: DragEvent, widget: WidgetDef) {
  event.dataTransfer?.setData('application/json', JSON.stringify(widget));
  emit('dragStart', widget);
}
</script>

<style scoped>
.widget-panel {
  height: 100%;
  border-right: 1px solid #f0f0f0;
  background: #fff;
}
.panel-title {
  padding: 12px 16px;
  font-weight: 600;
  border-bottom: 1px solid #f0f0f0;
}
.widget-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  padding: 8px;
}
.widget-item {
  padding: 8px 12px;
  border: 1px solid #d9d9d9;
  border-radius: 4px;
  cursor: grab;
  font-size: 12px;
  background: #fafafa;
  transition: all 0.2s;
}
.widget-item:hover {
  border-color: #1890ff;
  background: #e6f7ff;
}
.widget-item:active {
  cursor: grabbing;
}
</style>

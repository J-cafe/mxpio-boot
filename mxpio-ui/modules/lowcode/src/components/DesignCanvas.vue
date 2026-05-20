<template>
  <div
    class="design-canvas"
    @drop="onDrop"
    @dragover.prevent
    @dragenter.prevent
  >
    <div v-if="widgets.length === 0" class="canvas-placeholder">
      从左侧拖拽组件到此处
    </div>

    <div class="canvas-content">
      <div
        v-for="(row, ri) in rows"
        :key="ri"
        class="canvas-row"
      >
        <div
          v-for="(col, ci) in row.cols"
          :key="ci"
          class="canvas-col"
          :style="{ flex: `0 0 ${((col.span || 24) / 24) * 100}%` }"
        >
          <div
            class="widget-slot"
            :class="{ 'widget-slot-active': selectedId === col.widget?.id }"
            @click.stop="selectWidget(col.widget)"
          >
            <template v-if="col.widget">
              <div class="widget-header">
                <span class="widget-type">{{ col.widget.type }}</span>
                <a-button
                  type="link"
                  size="small"
                  danger
                  @click.stop="removeWidget(ri, ci)"
                >
                  ×
                </a-button>
              </div>
              <div class="widget-preview">
                <!-- DataGrid 预览 -->
                <div v-if="col.widget.type === 'DataGrid'" class="preview-grid">
                  <a-table
                    :columns="(col.widget.columns || [{title:'ID',dataIndex:'id'},{title:'名称',dataIndex:'name'}]).map((c:any) => ({title:c.title,dataIndex:c.field||c.dataIndex,key:c.field||c.dataIndex}))"
                    :dataSource="[]"
                    size="small"
                    :pagination="false"
                  />
                </div>
                <!-- AutoForm 预览 -->
                <div v-else-if="col.widget.type === 'AutoForm'" class="preview-form">
                  <a-form :label-col="{ span: 6 }" size="small">
                    <a-form-item label="示例字段">
                      <a-input placeholder="自动生成" disabled />
                    </a-form-item>
                  </a-form>
                </div>
                <!-- DataPilot 预览 -->
                <div v-else-if="col.widget.type === 'DataPilot'" class="preview-pilot">
                  <a-space>
                    <a-button size="small" type="primary">+ 新增</a-button>
                    <a-button size="small" danger>- 删除</a-button>
                    <a-button size="small">保存</a-button>
                  </a-space>
                </div>
                <!-- 通用 -->
                <div v-else class="preview-generic">
                  {{ col.widget.type }}
                </div>
              </div>
            </template>
            <template v-else>
              <div class="widget-empty">拖放组件</div>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { Table, Form, FormItem, Input, Button, Space } from 'ant-design-vue';

export interface CanvasWidget {
  id: string;
  type: string;
  dataSet?: string;
  columns?: Array<{ field: string; title: string; width?: number }>;
  colProps?: { span?: number };
  [key: string]: any;
}

export interface CanvasRow {
  cols: Array<{
    span: number;
    widget: CanvasWidget | null;
  }>;
}

const props = defineProps<{
  rows: CanvasRow[];
  selectedId: string | null;
}>();

const emit = defineEmits<{
  (e: 'update:rows', rows: CanvasRow[]): void;
  (e: 'select', id: string | null): void;
}>();

const widgets = computed(() => {
  const result: CanvasWidget[] = [];
  for (const row of props.rows) {
    for (const col of row.cols) {
      if (col.widget) result.push(col.widget);
    }
  }
  return result;
});

function selectWidget(widget: CanvasWidget | null) {
  emit('select', widget?.id || null);
}

function removeWidget(ri: number, ci: number) {
  const newRows = JSON.parse(JSON.stringify(props.rows));
  newRows[ri].cols[ci].widget = null;
  emit('update:rows', newRows);
}

function onDrop(event: DragEvent) {
  try {
    const data = event.dataTransfer?.getData('application/json');
    if (!data) return;
    const widgetDef = JSON.parse(data);

    // 在最后一行添加新组件
    let newRows = JSON.parse(JSON.stringify(props.rows));
    if (newRows.length === 0) {
      newRows = [{ cols: [{ span: 24, widget: null }] }];
    }

    // 找第一个空位
    let placed = false;
    for (const row of newRows) {
      for (const col of row.cols) {
        if (!col.widget) {
          col.widget = {
            id: `w_${Date.now()}`,
            type: widgetDef.type,
            dataSet: 'dsMain',
            ...widgetDef.defaultProps,
          };
          placed = true;
          break;
        }
      }
      if (placed) break;
    }

    // 没有空位则追加新行
    if (!placed) {
      newRows.push({
        cols: [
          {
            span: 24,
            widget: {
              id: `w_${Date.now()}`,
              type: widgetDef.type,
              dataSet: 'dsMain',
              ...widgetDef.defaultProps,
            },
          },
        ],
      });
    }

    emit('update:rows', newRows);
  } catch (e) {
    console.error('拖拽解析失败:', e);
  }
}
</script>

<style scoped>
.design-canvas {
  min-height: 400px;
  background: #f5f5f5;
  padding: 16px;
}
.canvas-placeholder {
  text-align: center;
  padding: 80px 0;
  color: #999;
  font-size: 14px;
}
.canvas-row {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
  min-height: 80px;
}
.canvas-col {
  flex: 1;
}
.widget-slot {
  border: 2px dashed #d9d9d9;
  border-radius: 4px;
  padding: 8px;
  min-height: 60px;
  background: #fff;
  cursor: pointer;
  transition: border-color 0.2s;
}
.widget-slot:hover {
  border-color: #1890ff;
}
.widget-slot-active {
  border-color: #1890ff;
  border-style: solid;
  box-shadow: 0 0 0 2px rgba(24, 144, 255, 0.2);
}
.widget-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4px;
}
.widget-type {
  font-size: 11px;
  color: #666;
  background: #f0f0f0;
  padding: 1px 6px;
  border-radius: 2px;
}
.widget-preview {
  pointer-events: none;
}
.widget-empty {
  text-align: center;
  color: #bbb;
  font-size: 12px;
  padding: 20px;
}
.preview-generic {
  text-align: center;
  padding: 16px;
  color: #999;
  background: #fafafa;
  border-radius: 2px;
}
</style>

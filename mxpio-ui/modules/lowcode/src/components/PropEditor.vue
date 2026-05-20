<template>
  <div class="prop-editor">
    <div class="panel-title">属性配置</div>
    <a-empty v-if="!widget" description="选择画布中的组件以编辑属性" />
    <a-form v-else layout="vertical" size="small">
      <a-form-item label="组件ID">
        <a-input :value="widget.id" disabled />
      </a-form-item>
      <a-form-item label="组件类型">
        <a-input :value="widget.type" disabled />
      </a-form-item>
      <a-form-item label="绑定DataSet">
        <a-input
          :value="widget.dataSet"
          @change="(e: any) => update('dataSet', e.target.value)"
        />
      </a-form-item>

      <!-- DataGrid 独有属性 -->
      <template v-if="widget.type === 'DataGrid'">
        <a-divider>列表列配置</a-divider>
        <div
          v-for="(col, idx) in (widget.columns || [])"
          :key="idx"
          class="column-item"
        >
          <a-input
            :value="col.field"
            placeholder="字段名"
            size="small"
            style="width: 40%"
            @change="(e: any) => updateColumn(idx, 'field', e.target.value)"
          />
          <a-input
            :value="col.title"
            placeholder="列标题"
            size="small"
            style="width: 40%"
            @change="(e: any) => updateColumn(idx, 'title', e.target.value)"
          />
          <a-button
            type="link"
            size="small"
            danger
            @click="removeColumn(idx)"
          >
            ×
          </a-button>
        </div>
        <a-button type="dashed" size="small" block @click="addColumn">
          + 添加列
        </a-button>
      </template>

      <!-- AutoForm 独有属性 -->
      <template v-if="widget.type === 'AutoForm'">
        <a-form-item label="标签宽度">
          <a-input-number
            :value="widget.labelCol || 6"
            :min="2"
            :max="24"
            @change="(v: number) => update('labelCol', v)"
          />
        </a-form-item>
      </template>

      <!-- Row 独有属性 -->
      <template v-if="widget.type === 'Row'">
        <a-form-item label="列数">
          <a-input-number
            :value="widget.colCount || 2"
            :min="1"
            :max="4"
            @change="(v: number) => update('colCount', v)"
          />
        </a-form-item>
      </template>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { Form, FormItem, Input, InputNumber, Divider, Empty } from 'ant-design-vue';
import type { CanvasWidget } from './DesignCanvas.vue';

const props = defineProps<{
  widget: CanvasWidget | null;
}>();

const emit = defineEmits<{
  (e: 'update', widget: CanvasWidget): void;
}>();

function update(key: string, value: any) {
  if (!props.widget) return;
  const updated = { ...props.widget, [key]: value };
  emit('update', updated);
}

function updateColumn(idx: number, key: string, value: string) {
  if (!props.widget?.columns) return;
  const columns = [...props.widget.columns];
  columns[idx] = { ...columns[idx], [key]: value };
  update('columns', columns);
}

function addColumn() {
  const columns = [...(props.widget?.columns || [])];
  columns.push({ field: '', title: '' });
  update('columns', columns);
}

function removeColumn(idx: number) {
  const columns = (props.widget?.columns || []).filter(
    (_: any, i: number) => i !== idx
  );
  update('columns', columns);
}
</script>

<style scoped>
.prop-editor {
  height: 100%;
  border-left: 1px solid #f0f0f0;
  background: #fff;
}
.panel-title {
  padding: 12px 16px;
  font-weight: 600;
  border-bottom: 1px solid #f0f0f0;
}
.column-item {
  display: flex;
  gap: 4px;
  margin-bottom: 4px;
}
</style>

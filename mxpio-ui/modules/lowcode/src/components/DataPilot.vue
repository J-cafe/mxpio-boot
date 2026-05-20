<template>
  <div class="lowcode-data-pilot">
    <a-space>
      <a-button type="primary" size="small" @click="handleAdd">
        <PlusOutlined /> 新增
      </a-button>
      <a-button
        type="primary"
        size="small"
        danger
        :disabled="!hasSelection"
        @click="handleDelete"
      >
        <DeleteOutlined /> 删除
      </a-button>
      <a-button size="small" @click="handleSave">
        <SaveOutlined /> 保存
      </a-button>
      <a-divider type="vertical" />
      <a-button size="small" :disabled="!hasPrev" @click="handlePrev">
        <LeftOutlined />
      </a-button>
      <span class="nav-info">
        {{ currentPosition }} / {{ total }}
      </span>
      <a-button size="small" :disabled="!hasNext" @click="handleNext">
        <RightOutlined />
      </a-button>
    </a-space>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import {
  PlusOutlined,
  DeleteOutlined,
  SaveOutlined,
  LeftOutlined,
  RightOutlined,
} from '@ant-design/icons-vue';
import { message } from 'ant-design-vue';
import type { DataSet } from '../core/DataSet';

const props = defineProps<{
  dataSet: DataSet;
  emptyRecord?: () => Record<string, any>;
}>();

const hasSelection = computed(
  () => props.dataSet?.currentRecord && props.dataSet.currentRecord['id']
);
const hasPrev = computed(() => (props.dataSet?.currentIndex ?? 0) > 0);
const hasNext = computed(
  () =>
    (props.dataSet?.currentIndex ?? 0) < (props.dataSet?.data.length ?? 0) - 1
);
const total = computed(() => props.dataSet?.data.length ?? 0);
const currentPosition = computed(
  () => (props.dataSet?.currentIndex ?? -1) + 1
);

function handleAdd() {
  const record = props.emptyRecord
    ? props.emptyRecord()
    : Object.create(null);
  props.dataSet.insert(record);
}

function handleDelete() {
  const record = props.dataSet.currentRecord;
  if (record && record['id']) {
    props.dataSet.remove(record['id']);
  }
}

async function handleSave() {
  try {
    await props.dataSet.submit();
    message.success('保存成功');
    await props.dataSet.load(props.dataSet['criteria']);
  } catch (e: any) {
    message.error('保存失败: ' + (e?.message || '未知错误'));
  }
}

function handlePrev() {
  props.dataSet.prev();
}

function handleNext() {
  props.dataSet.next();
}
</script>

<style scoped>
.lowcode-data-pilot {
  padding: 4px 0;
}
.nav-info {
  font-size: 12px;
  color: #999;
  min-width: 40px;
  text-align: center;
  display: inline-block;
}
</style>

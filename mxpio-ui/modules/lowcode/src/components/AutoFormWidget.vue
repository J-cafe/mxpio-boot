<template>
  <div class="lowcode-auto-form">
    <Form
      :model="formData"
      :label-col="{ span: 6 }"
      :wrapper-col="{ span: 18 }"
    >
      <template v-for="field in fields" :key="field.propertyCode">
        <Form.Item
          :label="field.propertyName"
          :name="field.propertyCode"
          :rules="buildRules(field)"
        >
          <component
            :is="resolveComponent(field.showType)"
            v-model:value="formData[field.propertyCode]"
            v-bind="getComponentProps(field)"
          />
        </Form.Item>
      </template>
    </Form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, watch, computed } from 'vue';
import {
  Form,
  Input,
  InputNumber,
  DatePicker,
  Select,
  Switch,
  Textarea,
} from 'ant-design-vue';
import type { DataSet } from '../core/DataSet';

interface FieldConfig {
  propertyCode: string;
  propertyName: string;
  showType: string;
  dictCode?: string;
  requiredFlag?: boolean;
  editableFlag?: boolean;
}

const props = defineProps<{
  dataSet: DataSet;
  fields: FieldConfig[];
}>();

const formData = reactive<Record<string, any>>({});

watch(
  () => props.dataSet?.currentRecord,
  (record) => {
    if (record) {
      Object.keys(formData).forEach((k) => delete formData[k]);
      for (const field of props.fields) {
        formData[field.propertyCode] = record[field.propertyCode];
      }
    }
  },
  { immediate: true }
);

watch(
  formData,
  () => {
    const record = props.dataSet?.currentRecord;
    if (record && record['id']) {
      const changes: Record<string, any> = {};
      for (const field of props.fields) {
        if (formData[field.propertyCode] !== record[field.propertyCode]) {
          changes[field.propertyCode] = formData[field.propertyCode];
        }
      }
      if (Object.keys(changes).length > 0) {
        props.dataSet.update(record['id'], changes);
      }
    }
  },
  { deep: true }
);

function resolveComponent(showType: string): any {
  const map: Record<string, any> = {
    TEXT: Input,
    TEXTAREA: Textarea,
    NUMBER: InputNumber,
    DATE: DatePicker,
    DATETIME: DatePicker,
    CHECKBOX: Switch,
    SELECT: Select,
  };
  return map[showType] || Input;
}

function getComponentProps(field: FieldConfig): Record<string, any> {
  const props: Record<string, any> = {};
  if (field.showType === 'TEXTAREA') {
    props.rows = 3;
  }
  if (field.showType === 'DATETIME') {
    props.showTime = true;
  }
  if (!field.editableFlag) {
    props.disabled = true;
  }
  return props;
}

function buildRules(field: FieldConfig): any[] {
  const rules: any[] = [];
  if (field.requiredFlag) {
    rules.push({ required: true, message: `请输入${field.propertyName}` });
  }
  return rules;
}
</script>

<style scoped>
.lowcode-auto-form {
  padding: 16px;
}
</style>

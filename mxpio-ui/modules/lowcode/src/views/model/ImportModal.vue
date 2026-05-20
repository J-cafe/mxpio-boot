<template>
  <BasicModal v-bind="$attrs" title="导入Entity" @register="registerModal" @ok="handleOk">
    <a-select
      v-model:value="selectedEntity"
      placeholder="选择Entity类"
      style="width: 100%"
      showSearch
      :options="entityOptions"
      :loading="loading"
    />
  </BasicModal>
</template>

<script lang="ts" setup>
import { ref } from 'vue';
import { BasicModal, useModalInner } from '@mxpio/components';
import { useBridge } from '@mxpio/bridge';
import { message } from 'ant-design-vue';

const { http } = useBridge();
const emit = defineEmits(['success']);
const [registerModal, { closeModal }] = useModalInner();

const selectedEntity = ref<string>();
const entityOptions = ref<Array<{ label: string; value: string }>>([]);
const loading = ref(false);

async function handleOk() {
  if (!selectedEntity.value) {
    message.warning('请选择一个Entity类');
    return;
  }
  try {
    loading.value = true;
    await http.post({
      url: '/lowcode/model/import',
      params: { entityClass: selectedEntity.value },
    });
    message.success('导入成功');
    selectedEntity.value = undefined;
    emit('success');
    closeModal();
  } catch (e: any) {
    message.error('导入失败: ' + (e?.message || ''));
  } finally {
    loading.value = false;
  }
}

// 打开弹窗时加载Entity列表
useModalInner(async () => {
  const res: any = await http.get({ url: '/lowcode/model/available/entities' });
  entityOptions.value = (res || []).map((c: string) => ({ label: c, value: c }));
});
</script>

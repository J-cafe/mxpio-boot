<template>
  <div>
    <a-input-search
      v-if="!multiple"
      :placeholder="placeholder"
      :value="value"
      :disabled="disabled"
      readonly
      allowClear
      @search="showSelectModal"
    />
    <div v-else>
      <a type="link" @click="showSelectModal">选择</a>
      <a-divider type="vertical" />
      <a-popover title="已选择批次">
        <template #content>
          <PopoverTable :dataSource="selectionRows" @remove="hanldleRemove" />
        </template>
        <a>查看:{{ selectionRows.length }}条</a>
      </a-popover>
    </div>
    <LotExecutedModal
      v-bind="$attrs"
      :multiple="multiple"
      @register="registerModal"
      @success="handleSuccess"
    />
  </div>
</template>

<script lang="ts" setup>
  import { Popover as APopover } from 'ant-design-vue';
  import { useModal } from '@mxpio/components';
  import LotExecutedModal from './LotExecutedModal.vue';
  import PopoverTable from './PopoverTable.vue';
  import { ref, onMounted } from 'vue';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'InvLotSelect' });

  const emit = defineEmits(['update:value', 'change', 'select']);

  const props = defineProps({
    value: { type: String },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: '选择已执行批次' },
    multiple: { type: Boolean, default: true },
    params: { type: Object, default: () => ({}) },
  });

  const selectionRows = ref<any[]>([]);
  const [registerModal, { openModal }] = useModal();
  // 打开选择用户弹窗
  function showSelectModal() {
    openModal(true, {});
  }

  // 选择批次成功
  function handleSuccess(ids: string, rows: Recordable[]) {
    selectionRows.value = rows;
    XEUtils.set(props.params, 'selectLot', rows);
    emit('update:value', ids, rows);
    emit('change', ids, rows);
    // change方法会在表单赋值时触发，需要监听选择事件，请使用onSelect方法
    emit('select', ids, rows);
  }

  // 移除
  function hanldleRemove(item: Recordable) {
    selectionRows.value = selectionRows.value.filter((row) => row.lotNo !== item.lotNo);
    XEUtils.set(props.params, 'selectLot', selectionRows.value);
    const ids = selectionRows.value?.map((item) => item.lotNo);
    emit('update:value', ids, selectionRows.value);
    emit('change', ids, selectionRows.value);
  }

  onMounted(() => {
    selectionRows.value = props.params?.selectLot || [];
  });
</script>

<template>
  <a-input-search
    placeholder="请选择"
    :value="inputDisplayValue"
    :disabled="disabled"
    readonly
    allowClear
    @search="showSelectModal"
  />
  <BizSelectModal
    ref="selectBizModal"
    v-bind="$attrs"
    :value="value"
    :rowKey="rowKey"
    :displayKey="displayKey"
    :multiple="multiple"
    :columns="columns"
    :listUrl="listUrl"
    :title="title"
    @register="registerModal"
    @success="handleSuccess"
  />
</template>

<script lang="ts" setup>
  import { useModal } from '../../../../Modal';
  import BizSelectModal from './modules/BizSelectModal.vue';
  import { ref, Ref, watch, computed } from 'vue';
  import { getAction } from '@mxpio/api';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'BizSelect' });

  const props = defineProps({
    value: { type: String },
    disabled: { type: Boolean, default: false },
    // 是否支持多选，默认 true
    multiple: {
      type: Boolean,
      default: true,
    },
    // 显示的 Key，传入后组件内部自动根据 value 调用接口查询显示文本
    displayKey: {
      type: String,
      default: null,
    },
    // 表格行 Key，默认 id
    rowKey: {
      type: String,
      default: 'id',
    },
    // 列表接口地址
    listUrl: {
      type: String,
      default: '',
    },
    // 弹窗表格列配置
    columns: {
      type: Array,
      default: () => [],
    },
    title: {
      type: String,
      default: '',
    },
    isTransform: {
      type: Boolean,
      default: true,
    },
  });

  const selectNames = ref(''); // 回显数据
  const selectKeys: Ref<string[]> = ref([]); // 已选择的数组
  const isInternalChange = ref(false); // 标记是否为组件内部触发的 value 变更
  const emit = defineEmits(['update:value', 'select']);

  const [registerModal, { openModal }] = useModal();

  // 输入框显示值：配置了 displayKey 时显示翻译后的文本，否则显示原始 value
  const inputDisplayValue = computed(() => {
    if (props.displayKey && selectNames.value) return selectNames.value;
    return props.value || '';
  });

  // 监听 value 外部变化，自动调用接口翻译显示文本
  watch(
    () => props.value,
    async (newVal) => {
      if (!props.isTransform) return;
      if (isInternalChange.value) {
        isInternalChange.value = false;
        return;
      }
      if (newVal && props.displayKey && props.listUrl) {
        await fetchDisplayNames(newVal);
      } else if (!newVal) {
        selectNames.value = '';
      }
    },
    { immediate: true },
  );

  // 根据绑定的 key 值调用接口查询显示文本
  async function fetchDisplayNames(val: string) {
    try {
      const ids = val.split(',').filter(Boolean);
      if (ids.length === 0) {
        selectNames.value = '';
        return;
      }
      const params = getVxeTableQueryParams({
        page: { pageSize: 99, currentPage: 1, total: 0 },
        filters: { [`${props.rowKey}@IN`]: ids },
      });
      const res = await getAction(props.listUrl, params);
      if (res.content && res.content.length > 0) {
        const list = res.content.map((item: any) => item[props.displayKey!]);
        selectNames.value = list.join(',');
      } else {
        selectNames.value = '';
      }
    } catch {
      selectNames.value = '';
    }
  }

  // 打开选择弹窗
  function showSelectModal() {
    openModal(true, {});
  }

  // 选择数据
  function handleSuccess(selectKeys_, selectRows) {
    selectKeys.value = selectKeys_;
    isInternalChange.value = true;
    emit('update:value', selectKeys_.join(','), selectRows);
    emit('select', selectKeys_.join(','), selectRows);
    if (!props.isTransform) return;
    getSelectNames(selectRows);
  }

  // 从已选行中提取显示字段
  async function getSelectNames(selectRows) {
    const selectList: any[] = [];
    selectRows.forEach((item) => {
      selectList.push(item[props.displayKey || props.rowKey]);
    });
    selectNames.value = selectList.join(',');
  }
</script>

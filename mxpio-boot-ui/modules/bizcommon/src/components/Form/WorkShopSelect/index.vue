<template>
  <Select
    class="!w-full"
    v-bind="$attrs"
    @change="handleChange"
    :options="getOptions"
    v-model:value="state"
    @dropdown-visible-change="handleFetch"
  >
    <template #[item]="data" v-for="item in Object.keys($slots)">
      <slot :name="item" v-bind="data || {}"></slot>
    </template>
    <template #suffixIcon v-if="loading">
      <LoadingOutlined spin />
    </template>
    <template #notFoundContent v-if="loading">
      <span>
        <LoadingOutlined spin class="mr-1" />
        {{ $t('component.form.apiSelectNotFound') }}
      </span>
    </template>
  </Select>
</template>
<script lang="ts" setup>
  import { PropType, ref, computed, unref, watch, onMounted } from 'vue';
  import { Select } from 'ant-design-vue';
  import type { SelectValue } from 'ant-design-vue/es/select';
  import { useRuleFormItem } from '@mxpio/hooks/src/component/useFormItem';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { $t } from '@mxpio/locales';
  import { propTypes } from '@mxpio/utils/src/propTypes';
  import { getQueryParams } from '@mxpio/utils';
  import { workshopList } from '../../../api';

  type OptionsItem = { label?: string; value?: string; disabled?: boolean; [name: string]: any };

  defineOptions({ name: 'WorkShopSelect', inheritAttrs: false });

  const props = defineProps({
    value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
    params: propTypes.any.def({}),
    alwaysLoad: propTypes.bool.def(false),
    filters: propTypes.any.def({}) || propTypes.func,
  });

  const emit = defineEmits(['options-change', 'change', 'update:value']);
  const optionsRef = ref<OptionsItem[]>([]);

  const loading = ref(false);
  // 首次是否加载过了
  const isFirstLoaded = ref(false);
  const emitData = ref<OptionsItem[]>([]);

  // Embedded in the form, just use the hook binding to perform form verification
  const [state] = useRuleFormItem(props, 'value', 'change', emitData);

  const getOptions = computed(() => {
    let data = unref(optionsRef).map((item) => {
      return {
        label: item.workShopName,
        value: item.workShopCode,
        item: item,
      };
    }, [] as OptionsItem[]);
    return data;
  });

  watch(
    () => state.value,
    (v) => {
      emit('update:value', v);
    },
  );

  async function fetch() {
    optionsRef.value = [];
    try {
      loading.value = true;
      let filters = props.filters;
      // 处理参数
      if (typeof props.filters === 'function') {
        filters = await props.filters(props.params);
      }
      const params = getQueryParams({}, filters);
      const res = await workshopList(params);
      isFirstLoaded.value = true;
      if (Array.isArray(res)) {
        optionsRef.value = res;
        emitChange();
        return;
      }
      emitChange();
    } catch (error) {
      console.warn(error);
    } finally {
      loading.value = false;
      // reset status
      isFirstLoaded.value = false;
    }
  }

  function emitChange() {
    emit('options-change', unref(getOptions));
  }

  function handleChange(_, ...args) {
    emitData.value = args;
  }

  async function handleFetch(visible: boolean) {
    if (visible) {
      if (props.alwaysLoad) {
        await fetch();
      } else if (!props.alwaysLoad && unref(isFirstLoaded)) {
        await fetch();
      }
    }
  }

  onMounted(() => {
    fetch();
  });
</script>

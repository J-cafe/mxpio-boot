<template>
  <Select
    @dropdown-visible-change="handleFetch"
    v-bind="$attrs"
    @change="handleChange"
    :options="getOptions"
    v-model:value="state"
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
  import { PropType, ref, computed, unref, watch } from 'vue';
  import { Select } from 'ant-design-vue';
  import type { SelectValue } from 'ant-design-vue/es/select';
  import { isFunction } from '@mxpio/utils/src/is';
  import { useRuleFormItem } from '@mxpio/hooks/src/component/useFormItem';
  import { get, omit, isEqual } from 'lodash-es';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { $t } from '@mxpio/locales';
  import { propTypes } from '@mxpio/utils/src/propTypes';
  import { getQueryParams } from '@mxpio/utils';

  type OptionsItem = { label?: string; value?: string; disabled?: boolean; [name: string]: any };

  defineOptions({ name: 'ApiSelect', inheritAttrs: false });
  const props = defineProps({
    value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
    numberToString: propTypes.bool,
    arrayToString: propTypes.bool.def(false), // 多选时，是否将数组转为字符串
    api: {
      type: Function as PropType<(arg?: any) => Promise<OptionsItem[]>>,
      default: null,
    },
    // api params
    params: propTypes.any.def({}),
    filters: propTypes.any.def({}) || propTypes.func,
    // support xxx.xxx.xx
    resultField: propTypes.string.def(''),
    labelField: propTypes.string.def('label'),
    valueField: propTypes.string.def('value'),
    immediate: propTypes.bool.def(true),
    alwaysLoad: propTypes.bool.def(false),
    options: {
      type: Array<OptionsItem>,
      default: [],
    },
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
    const { labelField, valueField, numberToString } = props;

    let data = unref(optionsRef).reduce((prev, next: any) => {
      if (next) {
        const value = get(next, valueField);
        prev.push({
          ...omit(next, [labelField, valueField]),
          label: get(next, labelField),
          value: numberToString ? `${value}` : value,
        });
      }
      return prev;
    }, [] as OptionsItem[]);
    return data.length > 0 ? data : props.options;
  });

  // watch(
  //   () => state.value,
  //   (v) => {
  //     // emit('update:value', v);
  //     if (state.value === '' || state.value === null || state.value === undefined) {
  //       emit('update:value', v);
  //       return;
  //     }
  //     const { arrayToString } = props;
  //     emit('update:value', arrayToString ? `${v}` : v);
  //   },
  // );

  watch(
    () => props.filters,
    (value, oldValue) => {
      if (isEqual(value, oldValue)) return;
      fetch();
    },
    { deep: true, immediate: props.immediate },
  );

  async function fetch() {
    const api = props.api;
    if (!api || !isFunction(api) || loading.value) return;
    optionsRef.value = [];
    try {
      loading.value = true;
      let filters = props.filters;
      // 处理参数
      if (typeof props.filters === 'function') {
        filters = await props.filters(props.params);
      }
      const params = getQueryParams({}, filters);
      // const params = getQueryParams(props.params);
      const res = await api(params);
      isFirstLoaded.value = true;
      console.log(res);
      if (Array.isArray(res)) {
        optionsRef.value = res;
        emitChange();
        return;
      }
      if (props.resultField) {
        optionsRef.value = get(res, props.resultField) || [];
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

  async function handleFetch(visible: boolean) {
    if (visible) {
      if (props.alwaysLoad) {
        await fetch();
      } else if (!props.immediate && !unref(isFirstLoaded)) {
        await fetch();
      }
    }
  }

  function emitChange() {
    emit('options-change', unref(getOptions));
  }

  function handleChange(_, ...args) {
    emitData.value = args;
  }
</script>

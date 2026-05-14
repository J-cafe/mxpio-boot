<template>
  <Select
    class="!w-full"
    allowClear
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
  import { PropType, ref, computed, unref, watch } from 'vue';
  import { Select } from 'ant-design-vue';
  import type { SelectValue } from 'ant-design-vue/es/select';
  import { useRuleFormItem } from '@mxpio/hooks/src/component/useFormItem';
  import { LoadingOutlined } from '@ant-design/icons-vue';
  import { $t } from '@mxpio/locales';
  import { propTypes } from '@mxpio/utils/src/propTypes';
  // import { getDictByCode } from '@mxpio/api';
  import { dictCache } from '../../utils/dictCache';

  type OptionsItem = { label?: string; value?: string; disabled?: boolean; [name: string]: any };

  defineOptions({ name: 'DictSelect', inheritAttrs: false });

  const props = defineProps({
    value: { type: [Array, Object, String, Number] as PropType<SelectValue> },
    numberToString: propTypes.bool.def(false),
    dictCode: {
      type: String,
      default: '',
      required: true,
    },
    alwaysLoad: propTypes.bool.def(false),
    arrayToString: propTypes.bool.def(false),
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
    const { numberToString } = props;

    let data = unref(optionsRef).map((item) => {
      return {
        label: item.label,
        value: numberToString ? `${item.value}` : item.value,
        // disabled: item.itemStatus === '0',
        disabled: item.disabled,
      };
    }, [] as OptionsItem[]);
    return data;
  });

  // watch(
  //   () => state.value,
  //   (v) => {
  //     if (state.value === '' || state.value === null || state.value === undefined) {
  //       emit('update:value', state.value);
  //       return;
  //     }
  //     const { arrayToString } = props;
  //     emit('change', arrayToString ? `${v}` : v);
  //     emit('update:value', arrayToString ? `${v}` : v);
  //   },
  // );

  watch(
    () => props.dictCode,
    (v) => {
      v && fetch();
    },
    {
      immediate: true,
    },
  );

  async function fetch() {
    optionsRef.value = [];
    try {
      loading.value = true;
      // const res = await getDictByCode(props.dictCode);
      // 从缓存中获取字典数据
      const items = await dictCache.get(props.dictCode);
      isFirstLoaded.value = true;
      optionsRef.value = items || [];
      // if (Array.isArray(items)) {
      //   optionsRef.value = items.sort((a, b) => {
      //     return a.itemSort - b.itemSort;
      //   });
      //   emitChange();
      //   return;
      // }
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
</script>

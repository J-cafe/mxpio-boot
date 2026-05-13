<template>
  <a-select
    class="w-full"
    labelInValue
    :value="selectedValue"
    :placeholder="placeholder"
    :disabled="disabled"
    :options="options"
    :filterOption="false"
    :autoClearSearchValue="true"
    :mode="mode"
    v-bind="$attrs"
    @change="handleChange"
    @search="handleSearch"
    @dropdown-visible-change="dropdownVisibleChange"
  >
    <template #dropdownRender="{ menuNode: menu }">
      <v-nodes :vnodes="menu" />
      <a-divider style="margin: 4px 0" />
      <a-pagination
        show-less-items
        v-model:current="current"
        v-bind="ipagination"
        :show-total="(total) => `共 ${total} 条记录`"
        @change="pageChange"
      />
    </template>
  </a-select>
</template>

<script lang="ts" setup>
  import { ref, onMounted, defineComponent, watch } from 'vue';
  import { Pagination as APagination } from 'ant-design-vue';
  import { getAction } from '@mxpio/api/src/common/manage';
  import { getQueryParams } from '@mxpio/utils/src/criteria';

  defineOptions({ name: 'ApiSelectPage' });

  const emit = defineEmits(['update:value', 'change']);

  const props = defineProps({
    value: [String, Number],
    placeholder: {
      type: String,
      default: '请选择',
    },
    url: {
      type: String,
      required: true,
    },
    optionKey: {
      type: Object,
      default: () => ({ label: 'label', value: 'value' }),
    },
    params: {
      type: Object,
      default: () => {},
    },
    filters: {
      type: Object,
      default: () => {},
    },
    disabled: Boolean,
    getPopupContainer: {
      type: Function,
      default: () => document.body,
    },
    mode: {
      type: String,
      default: 'default', // multiple || default
    },
    optionLabel: {
      type: String,
    },
    optionValue: {
      type: String,
    },
  });

  const dataSourceMap = new Map();
  const options = ref([]);
  const current = ref(1);
  const selectedValue = props.mode === 'multiple' ? ref([]) : ref({});
  const selectedValueStr = ref('');

  const ipagination = ref({
    total: 0,
    pageSize: 10,
  });

  const VNodes = defineComponent({
    props: {
      vnodes: {
        type: Object,
        required: true,
      },
    },
    render() {
      return this.vnodes;
    },
  });

  watch(
    () => props.value,
    (val) => {
      if (val === '' || val === null || val === undefined) {
        selectedValue.value = props.mode === 'multiple' ? [] : {};
        return;
      }
      // 初始化选中值，当手动设置value值时，需要初始化选中值
      if (val !== selectedValueStr.value) {
        initSelectValue(val);
      }
    },
    {
      immediate: true,
    },
  );

  // 获取列表
  async function loadData(searchValue?: string) {
    // 组装分页及搜索条件
    const params = getQueryParams(
      Object.assign(
        {},
        { page: current.value, size: ipagination.value.pageSize },
        searchValue ? { [props.optionLabel || props.optionKey.label]: searchValue } : {},
      ),
      props.filters,
    );
    // 获取数据
    const res = await getAction(props.url, params);
    // 设置分页总条数
    ipagination.value.total = res.totalElements;
    // 列表数据转换为map,方便后续取值使用
    dataSourceMapFn(res.content);
    // 列表数据转换为options
    const resData = res.content.map((item: Recordable) => {
      return {
        label: item[props.optionLabel || props.optionKey.label],
        value: item[props.optionValue || props.optionKey.value],
        optionData: item,
      };
    });
    options.value = resData;
  }

  // 列表数据转换为map,方便后续取值使用
  function dataSourceMapFn(dataSource) {
    dataSource.forEach((item) => {
      dataSourceMap.set(item[props.optionValue || props.optionKey.value], item);
    });
  }

  // 分页切换
  function pageChange(page: number, pageSize: number) {
    current.value = page;
    ipagination.value.pageSize = pageSize;
    loadData();
  }

  // 搜索
  function handleSearch(value: string) {
    current.value = 1;
    loadData(value);
  }

  // 下拉框打开
  function dropdownVisibleChange(open: boolean) {
    if (open) {
      loadData();
    }
  }

  // 值改变
  function handleChange(value) {
    // 重新赋值选中数据
    selectedValue.value = value;
    if (value instanceof Array) {
      const values = value.map((item) => {
        return item.value;
      });
      selectedValueStr.value = values.join(',');
      emit('update:value', values.join(','), value);
      emit('change', values.join(','), value);
    } else {
      selectedValueStr.value = value.value;
      emit('update:value', value.value, [value]);
      emit('change', value.value, [value]);
    }
  }

  // 初始化选中值
  async function initSelectValue(value) {
    // 组装查询条件
    const params = getQueryParams(
      Object.assign(
        {},
        { page: 1, size: 999 },
        { [`${props.optionValue || props.optionKey.value}@IN`]: value },
      ),
      props.filters,
    );
    // 根据查询条件获取选中数据
    const res = await getAction(props.url, params);
    // 列表数据转换为map,方便后续取值使用
    dataSourceMapFn(res.content);
    // 选中值格式化为数组
    const values = value.split(',');
    // 选中行数据
    const options: Recordable[] = [];
    values.forEach((key: string) => {
      const option = dataSourceMap.get(key);
      if (option) {
        // 组装select选中值
        options.push({
          label: option[props.optionKey.label],
          value: option[props.optionKey.value],
          optionData: option,
        });
      }
    });
    if (props.mode === 'multiple') {
      selectedValue.value = options;
    } else {
      selectedValue.value = options[0];
    }
  }

  onMounted(() => {
    loadData();
  });
</script>

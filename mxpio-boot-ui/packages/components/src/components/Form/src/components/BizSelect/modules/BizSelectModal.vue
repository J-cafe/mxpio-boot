<template>
  <BasicModal
    destroyOnClose
    :width="width"
    @register="registerModal"
    :title="title ? `请选择${title}` : '请选择'"
    @ok="handleSubmit"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <FormItem>
        <VxeBasicTable
          ref="tableRef"
          v-bind="gridOptions"
          @checkbox-change="handleCheckboxChange"
          @checkbox-all="handleCheckboxChange"
          @filter-change="filterChangeEvent"
        >
          <template #tools>
            <a-popover title="已选择" v-if="multiple" trigger="click">
              <template #content>
                <PopoverTable
                  style="width: 400px"
                  :dataSource="selectionRows"
                  :columns="columns"
                  @remove="handleDelete"
                />
              </template>
              <a style="margin-left: 10px">已选择:{{ selectionRows.length }}条</a>
            </a-popover>
          </template>
        </VxeBasicTable>
      </FormItem>
    </FormItemRest>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, defineProps, Ref, computed } from 'vue';
  import { BasicModal, useModalInner } from '../../../../../Modal';
  import { Popover as APopover, FormItemRest, FormItem } from 'ant-design-vue';
  import { getAction } from '@mxpio/api';
  import type { VxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import { getVxeTableQueryParams } from '@mxpio/utils/src/criteria';
  import PopoverTable from './PopoverTable.vue';
  import { VxeBasicTable } from '../../../../../VxeTable';
  import type { VxeGridPropTypes } from '../../../../../VxeTable';

  defineOptions({ name: 'BizSelectModal' });

  const props = defineProps({
    value: {
      type: String,
    },
    multiple: {
      type: Boolean,
      default: true,
    },
    width: {
      type: Number,
      default: 1200,
    },
    listUrl: {
      type: String,
      required: true,
      default: '',
    },
    columns: {
      type: Array,
      required: true,
      default: () => [],
    },
    rowKey: {
      type: String,
      default: 'id',
    },
    title: {
      type: String,
      default: '',
    },
    // 外部传入的过滤条件，支持对象或函数
    // 对象: { 'status@EQ': '1' }
    // 函数: () => ({ 'status@EQ': formValues.status })
    filters: {
      type: [Object, Function] as PropType<Recordable | (() => Recordable)>,
      default: () => ({}),
    },
  });

  const emit = defineEmits(['success', 'register']);
  const selectionRows: Ref<Recordable[]> = ref([]);
  const selectKeys: Ref<string[]> = ref([]);
  const tableRef = ref();
  const currentPage = ref(1);

  // 动态解析 filters，支持函数形式
  const resolvedFilters = computed<Recordable>(() => {
    if (typeof props.filters === 'function') {
      return props.filters() || {};
    }
    return props.filters || {};
  });
  const gridOptions = {
    id: 'BizSelectTable',
    tableClass: '!px-2 !py-0',
    columns: [
      {
        type: props.multiple ? 'checkbox' : 'radio',
        width: 40,
      },
      ...props.columns,
    ],
    toolbarConfig: {
      slots: {
        buttons: 'tools',
      },
    },
    height: 500,
    proxyConfig: {
      ajax: {
        query: async ({ page, sorts, filters }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, sorts, tableFilters: filters });
        },
      },
      autoLoad: false,
    },
    checkboxConfig: {
      trigger: 'cell',
      reserve: true,
    },
    radioConfig: {
      trigger: 'row',
    },
    rowConfig: {
      keyField: props.rowKey,
    },
    filterConfig: {
      remote: true,
    },
    pagerConfig: {
      currentPage: currentPage,
    },
  };

  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    tableRef.value?.commitProxy('query');
    props.multiple && (await initCheckboxRow());
  });

  // 获取列表
  async function loadData({ page, sorts, tableFilters }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      sorts,
      tableFilters,
      filters: resolvedFilters.value,
    });
    const res = await getAction(props.listUrl, params);
    return res;
  }

  // 过滤事件
  async function filterChangeEvent() {
    try {
      currentPage.value = 1;
      tableRef.value?.commitProxy('query');
    } catch (error) {
      console.log(error);
    }
  }

  // 已选列表删除
  function handleDelete(record) {
    selectionRows.value = selectionRows.value.filter(
      (row) => row[props.rowKey] !== record[props.rowKey],
    );
    tableRef.value?.setCheckboxRowKey(record[props.rowKey], false);
  }

  // 已选列表选择
  function handleCheckboxChange() {
    const rows: Recordable[] = tableRef.value?.getCheckboxRecords(true) || [];
    const reserveRows: Recordable[] = tableRef.value?.getCheckboxReserveRecords(true) || [];
    selectionRows.value = rows.concat(reserveRows);
  }

  // 选择
  async function handleSubmit() {
    try {
      const rows: Recordable[] = props.multiple
        ? tableRef.value
            ?.getCheckboxRecords(true)
            .concat(tableRef.value?.getCheckboxReserveRecords(true) || []) || []
        : tableRef.value?.getRadioRecord(true)
          ? [tableRef.value.getRadioRecord(true)]
          : [];
      const selectKeys_: string[] = rows.map((item: Recordable) => item[props.rowKey]);
      selectKeys.value = selectKeys_;
      closeModal();
      emit('success', selectKeys_, rows);
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 初始化已选列表
  async function initCheckboxRow() {
    const params = getVxeTableQueryParams({
      page: {
        pageSize: 99,
        currentPage: 1,
        total: 0,
      },
      filters: {
        ...resolvedFilters.value,
        [`${props.rowKey}@IN`]: props.value,
      },
    });
    const res = await getAction(props.listUrl, params);
    if (res.content && res.content.length > 0) {
      tableRef.value?.setCheckboxRow(res.content, true);
      selectionRows.value = res.content;
    } else {
      selectionRows.value = [];
    }
  }
</script>

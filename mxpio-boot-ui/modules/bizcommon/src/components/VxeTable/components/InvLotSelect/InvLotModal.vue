<template>
  <BasicModal
    destroyOnClose
    :maskClosable="false"
    width="1200px"
    @register="registerModal"
    title="请选择批次"
    :bodyStyle="{ padding: '2px' }"
  >
    <FormItemRest>
      <FormItem>
        <VxeBasicTable ref="tableRef" v-bind="gridOptions">
          <template #tools>
            <a-popover title="已选择批次" v-if="multiple">
              <template #content>
                <PopoverTable :dataSource="selectionRows" @remove="hanldleRemove" />
              </template>
              <a style="margin-left: 10px">已选择:{{ selectionRows.length }}条</a>
              <a-divider type="vertical" />
              <span>合计：{{ selectionTotal }}</span>
            </a-popover>
          </template>
        </VxeBasicTable>
      </FormItem>
    </FormItemRest>
    <template #footer>
      <a-button @click="closeModal">关闭</a-button>
      <a-button type="dashed" @click="handleTemp" v-if="multiple">暂存</a-button>
      <a-button type="primary" @click="handleSubmit">确定</a-button>
    </template>
  </BasicModal>
</template>
<script lang="ts" setup>
  import { ref, reactive, unref, computed } from 'vue';
  import { Popover as APopover, FormItemRest, FormItem } from 'ant-design-vue';
  import PopoverTable from './PopoverTable.vue';
  import {
    BasicModal,
    useModalInner,
    VxeBasicTable,
    VxeGridInstance,
    BasicVxeTableProps,
    VxeGridPropTypes,
  } from '@mxpio/components';
  import { lotPage } from '../../../../api/inventory/flow';
  import { getVxeTableQueryParams } from '@mxpio/utils';
  import type { VxeTableQueryParams } from '@mxpio/utils';
  import { useMessage } from '@mxpio/hooks';
  import { columns, searchFormSchema } from './invLot.data';
  import XEUtils from 'xe-utils';

  defineOptions({ name: 'InvLotModal' });

  const { createMessage } = useMessage();
  const props = defineProps({
    multiple: { type: Boolean, default: true },
    filters: { type: Object, default: () => ({}) },
    beforeSubmit: { type: Function, default: () => {} },
  });

  const emit = defineEmits(['success', 'register']);
  const selectionRows = ref<any[]>([]);
  const [registerModal, { setModalProps, closeModal }] = useModalInner(async () => {
    setModalProps({ confirmLoading: false });
    tableRef.value?.commitProxy('query');
  });

  const tableRef = ref<VxeGridInstance>();
  const gridOptions = reactive<BasicVxeTableProps>({
    id: 'InvLotSelectList',
    tableClass: '!px-2 !py-0',
    columns: [
      {
        type: props.multiple ? 'checkbox' : 'radio',
        width: 40,
      },
      ...columns,
    ],
    toolbarConfig: {
      slots: {
        buttons: 'tools',
      },
    },
    formConfig: {
      enabled: true,
      items: searchFormSchema,
    },
    minHeight: 500,
    proxyConfig: {
      ajax: {
        query: async ({ page, form, sorts }: VxeGridPropTypes.ProxyAjaxQueryParams<any>) => {
          return loadData({ page, form, sorts });
        },
      },
      autoLoad: false,
    },
    checkboxConfig: {
      trigger: 'cell',
    },
    radioConfig: {
      trigger: 'row',
    },
    editConfig: {
      trigger: 'click',
      mode: 'row',
      showStatus: true,
      autoClear: false,
    },
    editRules: {
      executeQuantity: [{ required: true, message: '请输入出库数量', trigger: 'change' }],
    },
    rowConfig: {
      keyField: 'id',
    },
  });

  async function loadData({ page, form, sorts }: VxeTableQueryParams) {
    const params = getVxeTableQueryParams({
      page,
      form,
      sorts,
      filters: Object.assign({}, props.filters),
    });
    const res = await lotPage(params);
    res.content = res.content.map((item) => {
      return { ...item, id: item.lotNo + '_' + item.subLotIndex };
    });
    afterLoad();
    return res;
  }

  // 加载数据后处理, 选中已选择的批次
  async function afterLoad() {
    setTimeout(() => {
      selectionRows.value.forEach((item) => {
        const row = tableRef.value?.getRowById(item.lotNo + '_' + item.subLotIndex);
        if (row) {
          tableRef.value?.setCheckboxRow(row, true);
          XEUtils.set(row, 'executeQuantity', item.executeQuantity);
        }
      });
    }, 500);
  }

  // 选择
  async function handleSubmit() {
    try {
      if (props.multiple) {
        const rows = tableRef.value?.getCheckboxRecords(true);
        if (rows?.length) {
          // 校验
          const errMap = await tableRef.value?.validate(rows);
          if (errMap) {
            return;
          }
          const combinedData = [...selectionRows.value, ...rows];
          selectionRows.value = removeDuplicateLots(combinedData) || [];
        }
        const ids = selectionRows.value?.map((item) => item.lotNo);
        if (props.beforeSubmit && typeof props.beforeSubmit === 'function') {
          await props.beforeSubmit(ids?.join(','), selectionRows.value);
        }
        emit('success', ids?.join(','), unref(selectionRows));
      } else {
        const row = tableRef.value?.getRadioRecord(true);
        if (row) {
          // 校验
          const errMap = await tableRef.value?.validate(row);
          if (errMap) {
            return;
          }
        }
        if (props.beforeSubmit && typeof props.beforeSubmit === 'function') {
          await props.beforeSubmit(row?.lotNo, row);
        }
        emit('success', row?.lotNo, row);
      }
      closeModal();
    } finally {
      setModalProps({ confirmLoading: false });
    }
  }

  // 暂存
  async function handleTemp() {
    try {
      const rows = tableRef.value?.getCheckboxRecords(true);
      if (!rows?.length) {
        return;
      }
      // 校验
      const errMap = await tableRef.value?.validate(rows);
      if (!errMap) {
        const combinedData = [...selectionRows.value, ...rows];
        selectionRows.value = removeDuplicateLots(combinedData) || [];
        createMessage.success('暂存成功');
      }
    } catch (err) {
      console.log(err);
    }
  }

  function hanldleRemove(row: any) {
    const index = selectionRows.value.findIndex(
      (item) => item.lotNo === row.lotNo && item.subLotIndex === row.subLotIndex,
    );
    if (index !== -1) {
      selectionRows.value.splice(index, 1);
    }
    tableRef.value?.setCheckboxRow(row, false);
  }

  /**
   * 去除重复批次数据
   * @param {Array} items - 需要去重的批次数据数组
   * @returns {Array} 去重后的批次数据数组
   */
  function removeDuplicateLots(items: Recordable) {
    const uniqueMap = new Map();
    // 确保输入是数组格式
    const itemsToProcess = Array.isArray(items) ? items : items ? [items] : [];
    itemsToProcess.forEach((item) => {
      if (item && item.lotNo !== undefined && item.subLotIndex !== undefined) {
        const uniqueKey = `${item.lotNo}${item.subLotIndex}`;
        uniqueMap.set(uniqueKey, { ...item });
      }
    });

    return Array.from(uniqueMap.values());
  }

  const selectionTotal = computed(() => {
    return selectionRows.value.reduce((total, item) => {
      return total + (item.executeQuantity || 0);
    }, 0);
  });
</script>
<style lang="less"></style>
